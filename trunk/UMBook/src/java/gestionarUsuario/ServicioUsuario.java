package gestionarUsuario;

import com.google.gson.Gson;
import gestionarPersistencia.Album;
import gestionarPersistencia.Ciudad;
import gestionarPersistencia.Grupo;
import gestionarPersistencia.Localidad;
import gestionarPersistencia.Usuario;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import persistencia.ConexionBD;

public class ServicioUsuario extends HttpServlet
{

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        ServletContext context = getServletContext();

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try
        {
            String accion = request.getParameter("accion") == null ? "" : request.getParameter("accion");
            EntityManager em = ConexionBD.getInstancia().getEm();

//loguearse----------------------------------------------------------------------
            if (accion.equals("login"))
            {

                Query consulta = em.createNamedQuery("Usuario.findAllByMailAndClave");
                consulta.setParameter("mail", request.getParameter("usuario-txt"));
                consulta.setParameter("clave", request.getParameter("clave-txt"));

                Usuario usuario = null;
                try
                {
                    usuario = (Usuario) consulta.getSingleResult();

                    if (usuario.getEstado() == true)
                    {
                        HttpSession sesion = request.getSession(true);
                        sesion.setAttribute("usuarioActual", usuario);
                        sesion.setAttribute("usuarioInvitado", usuario);
                        sesion.setAttribute("sesionIniciada", true);
                        out.write("{success:true}");
                    }
                    else
                    {
                        out.write("{failure:true,msg:\"Su cuenta ha sido desactivada temporalmente\"}");
                    }
                }
                catch (NoResultException e)
                {
                    out.write("{failure:true,msg:\"Mail o Clave incorrecta\"}");
                }
//registrarse----------------------------------------------------------------------
            }
            else
                if (accion.equals("registrarse"))
                {
                    try
                    {
                        Boolean sexo = true;
                        if (request.getParameter("sexo-txt").equals("0"))
                        {
                            sexo = false;
                        }

                        Usuario usuarioNuevo = new Usuario(
                                request.getParameter("mail-txt"),
                                request.getParameter("nombre-txt"),
                                request.getParameter("apellido-txt"),
                                request.getParameter("clave1-txt"),
                                Integer.valueOf(request.getParameter("idLocalidad")),
                                Integer.valueOf(request.getParameter("idCiudad")),
                                sexo,
                                new Date(request.getParameter("cumple-txt")),
                                "usr",
                                true,
                                null);

                        //VER CUMPLE***************************************************
                        usuarioNuevo.setCelular(request.getParameter("celular-txt"));
                        usuarioNuevo.setTelefono(request.getParameter("telefono-txt"));
                        usuarioNuevo.setCarrera(request.getParameter("carrera-txt"));

                        EntityTransaction transaccion = em.getTransaction();
                        Album albumPerfil = new Album("perfil", new Date());
                        albumPerfil.setDescripcion("Album creado por defecto");

                        transaccion.begin();
                        em.persist(usuarioNuevo);
                        transaccion.commit();
                        em.refresh(usuarioNuevo);

                        albumPerfil.setUsuario(usuarioNuevo);
                        transaccion.begin();
                        em.persist(albumPerfil);
                        transaccion.commit();
                        em.refresh(albumPerfil);
//--Crea la carpeta del usuario en el servidor-----------
                        File albums = new File(context.getInitParameter("directorioFotos") + usuarioNuevo.getId());
                        albums.mkdir();
//--Crea la carpeta del Album Perfil en el servidor-----------
                        File carpetaPerfil = new File(context.getInitParameter("directorioFotos") + usuarioNuevo.getId() + "/" + albumPerfil.getId());
                        carpetaPerfil.mkdir();
//--Crea el grupo Amigos en la DB-----------
                        Grupo grupoDefecto = new Grupo("amigos", "Todos los amigachos!!");
                        grupoDefecto.setUsuario(usuarioNuevo);
                        transaccion.begin();
                        em.persist(grupoDefecto);
                        transaccion.commit();

                        out.write("{success:true}");
                    }
                    catch (NumberFormatException e)
                    {
                        out.write("{failure:true,msg:\"error FORMATO DE NUMERO\"}");
                    }
                    catch (PersistenceException e)
                    {
                        out.write("{failure:true,msg:\"El usuario ingresado ya existe\"}");
                    }
                }//Cargar Perfil------------------------------------------------------
                else
                    if (accion.equals("cargarPerfil"))
                    {
                        try
                        {
                            HttpSession sesion = request.getSession(true);
                            EntityTransaction transaccion = em.getTransaction();
                            transaccion.begin();

                            Usuario usuario = em.find(Usuario.class, ((Usuario) sesion.getAttribute("usuarioActual")).getId());

                            Localidad localidad = ((Localidad) em.find(Localidad.class, usuario.getIdLocalidad()));
                            Ciudad ciudad = ((Ciudad) em.find(Ciudad.class, usuario.getIdCiudad()));

                            transaccion.commit();

                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                            String fecha = formatter.format(usuario.getFechaNac());
                            out.write("{success:true,data:");
                            out.write("[{nombre:\"" + usuario.getNombre()
                                    + "\",apellido:\"" + usuario.getApellido()
                                    + "\",mail:\"" + usuario.getMail()
                                    + "\",'localidad-txt':\"" + localidad.getNombre()
                                    + "\",'ciudad-txt':\"" + ciudad.getNombre() + "\"");
                            //Solucion para el radioGroup
                            if (usuario.getSexo())
                                out.write(",'sexo-txt':1");
                            out.write(",fechaNac:\"" + fecha
                                    + "\",telefono:\"" + usuario.getTelefono()
                                    + "\",celular:\"" + usuario.getCelular()
                                    + "\",carrera:\"" + usuario.getCarrera()
                                    + "\"}]");
                            out.write("}");
                        }
                        catch (NumberFormatException e)
                        {
                            out.write("{failure:true,msg:\"error FORMATO DE NUMERO\"}");
                        }
                        catch (PersistenceException e)
                        {
                            out.write("{failure:true,msg:\"Error de persistencia\"}");
                        }
//ACTUALIZAR CLAVE ----------------------------------------------------------------------
                    } //ACTUALIZAR PERFIL----------------------------------------------------------------------------
                    else
                        if (accion.equals("actualizarPerfil"))
                        {
                            try
                            {
                                HttpSession sesion = request.getSession(true);
                                EntityTransaction transaccion = em.getTransaction();
                                transaccion.begin();

                                Usuario usuarioNuevo = em.find(Usuario.class, ((Usuario) sesion.getAttribute("usuarioInvitado")).getId());
                                Localidad localidad = ((Localidad) sesion.getAttribute("usuarioLocalidad"));

                                Ciudad ciudad = em.find(Ciudad.class, usuarioNuevo.getIdCiudad());

                                //si llega un string, es porque no modifique ni la ciudad ni la localidad
                                if (!ciudad.getNombre().equals(request.getParameter("idCiudad")))
                                {
                                    usuarioNuevo.setIdCiudad(Integer.valueOf(request.getParameter("idCiudad")));
                                    usuarioNuevo.setIdLocalidad(Integer.valueOf(request.getParameter("idLocalidad")));
                                }

                                usuarioNuevo.setApellido(request.getParameter("apellido"));
                                usuarioNuevo.setNombre(request.getParameter("nombre"));
                                usuarioNuevo.setMail(request.getParameter("mail"));

                                if (request.getParameter("sexo-txt").equals("1"))
                                {
                                    usuarioNuevo.setSexo(true);
                                }
                                else
                                {
                                    usuarioNuevo.setSexo(false);
                                }
                                usuarioNuevo.setFechaNac(new Date(request.getParameter("fechaNac")));
                                usuarioNuevo.setCelular(request.getParameter("celular"));
                                usuarioNuevo.setTelefono(request.getParameter("telefono"));
                                usuarioNuevo.setCarrera(request.getParameter("carrera"));

                                transaccion.commit();
                                sesion.setAttribute("usuarioInvitado", usuarioNuevo);

                                out.write("{success:true}");
                            }
                            catch (NumberFormatException e)
                            {
                                out.write("{failure:true,msg:\"error FORMATO DE NUMERO\"}");
                            }
                            catch (PersistenceException e)
                            {
                                out.write("{failure:true,msg:\"Error de persistencia\"}");
                            }
//ACTUALIZAR CLAVE ----------------------------------------------------------------------
                        }
                        else
                            if (accion.equals("actualizarClave"))
                            {

                                HttpSession sesion = request.getSession(true);

                                try
                                {
                                    EntityTransaction transaccion = em.getTransaction();
                                    transaccion.begin();

                                    Usuario usuario = em.find(Usuario.class, ((Usuario) sesion.getAttribute("usuarioInvitado")).getId());

                                    if (usuario.getClave().equals(request.getParameter("claveActual")))
                                    {
                                        usuario.setClave(request.getParameter("claveNueva1"));
                                        transaccion.commit();
                                        out.write("{success:true}");
                                    }
                                    else
                                    {
                                        out.write("{failure:true,msg:\"La clave Actual no es la correcta\"}");
                                    }
                                }
                                catch (NumberFormatException e)
                                {
                                    out.write("{failure:true,msg:\"error FORMATO DE NUMERO\"}");
                                }
                                catch (PersistenceException e)
                                {
                                    out.write("{failure:true,msg:\"Error de persistencia\"}");
                                }

//VOLVER A MI MURO----------------------------------------------------------------------
                            }
                            else
                                if (accion.equals("cerrarSesion"))
                                {

                                    HttpSession sesion = request.getSession(true);
                                    sesion.invalidate();
                                    response.sendRedirect("index.jsp");

//VOVLER A MI MURO----------------------------------------------------------------------
                                }
                                else
                                    if (accion.equals("volverMuro"))
                                    {

                                        HttpSession sesion = request.getSession(true);
                                        sesion.setAttribute("usuarioActual", sesion.getAttribute("usuarioInvitado"));
                                        response.sendRedirect("elmuro.jsp");
                                    }
                                    else
                                        if (accion.equals("cargarCiudad"))
                                        {
                                            Query consulta = em.createNamedQuery("Ciudad.findAll");
                                            try
                                            {
                                                List<Ciudad> ciudades = consulta.getResultList();
                                                Gson jsonTraductor = new Gson();
                                                String jsonCiudades = jsonTraductor.toJson(ciudades);
                                                out.write("{ciudades:" + jsonCiudades + "}");
                                            }
                                            catch (NoResultException e)
                                            {
                                                out.write("{failure:true,msg:\"error en la consulta de ciudad\"}");
                                            }
                                            catch (NumberFormatException e)
                                            {
                                                out.write("{failure:true,msg:\"error FORMATO DE NUMERO\"}");
                                            }
//cargar localidad----------------------------------------------------------------------
                                        }
                                        else
                                            if (accion.equals("cargarLocalidad"))
                                            {

                                                Query consulta = em.createNamedQuery("Localidad.findByIdCiudad");
                                                try
                                                {
                                                    consulta.setParameter("idCiudad", Integer.valueOf(request.getParameter("idCiudad")));
                                                    List<Localidad> localidades = consulta.getResultList();
                                                    Gson jsonTraductor = new Gson();
                                                    String jsonLocalidades = jsonTraductor.toJson(localidades);
                                                    out.write("{localidades:" + jsonLocalidades + "}");
                                                }
                                                catch (NoResultException e)
                                                {
                                                    out.write("{failure:true,msg:\"error en la consulta de localidad\"}");
                                                }
                                                catch (NumberFormatException e)
                                                {
                                                    out.write("{failure:true,msg:\"error FORMATO DE NUMERO\"}");
                                                }
                                            }
        }
        finally
        {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo()
    {
        return "Short description";
    }// </editor-fold>
}
