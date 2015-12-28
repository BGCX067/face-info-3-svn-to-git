package gestionarAmigos;

import gestionarPersistencia.Grupo;
import gestionarPersistencia.Usuario;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import persistencia.ConexionBD;

public class ServicioAmigos extends HttpServlet
{

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try
        {

//CONTROLO SESION-----------------------------------------------
            HttpSession sesion = request.getSession(true);
            Usuario usuarioActual = (Usuario) sesion.getAttribute("usuarioActual");
            Boolean sesionIniciada = (Boolean) sesion.getAttribute("sesionIniciada");
            if (sesionIniciada != true || usuarioActual.getId() == null)
            {
                response.sendRedirect("index.jsp");
            }
            else
            {
                String accion = request.getParameter("accion") == null ? "" : request.getParameter("accion");
                EntityManager em = ConexionBD.getInstancia().getEm();

//Cargar Comentarios----------------------------------------------------------------------
                if (accion.equals("cargarAmigos")) {
                    Query consulta = null;
                    if (request.getParameter("idGrupo").equals("todos")) {
                        consulta = em.createNamedQuery("Grupo.findAllByIdUsuario");
                        consulta.setParameter("idUsuario", usuarioActual.getId());
                        List<Usuario> listaCompleta = new ArrayList<Usuario>();
                        List<Grupo> grupos = consulta.getResultList();
                        List<Usuario> usuariosXGrupo = null;

                        for (Grupo grupo : grupos) {
                            consulta = em.createNamedQuery("Grupo.findAllAmigos");
                            consulta.setParameter("usuarioActual", usuarioActual.getId());
                            consulta.setParameter("idGrupo", grupo.getId());
                            usuariosXGrupo = consulta.getResultList();
                            for (Usuario usuario : usuariosXGrupo) {
                                listaCompleta.add(usuario);
                            }
                        }
                        enviarJson(listaCompleta, request, out);
                    } else {
                        consulta = em.createNamedQuery("Grupo.findAllAmigos");
                        consulta.setParameter("usuarioActual", usuarioActual.getId());
                        consulta.setParameter("idGrupo", Integer.valueOf(request.getParameter("idGrupo")));
                        List usuarios = consulta.getResultList();
                        enviarJson(usuarios, request, out);
                    }
//VISITAR AMIGOS----------------------------------------------------------------------
                }
                else
                    if (accion.equals("visitarAmigo"))
                    {
                        Query consulta = em.createNamedQuery("GrupoUsuario.findAmigo");
                        int idUsuario = Integer.valueOf(request.getParameter("idVisitado"));
                        sesion.setAttribute("usuarioActual", em.find(Usuario.class, idUsuario));
                        out.write("{success:true}");

//BUSCAR AMIGOS----------------------------------------------------------------------
                    }
                    else
                        if (accion.equals("buscarAmigos"))
                        {

                            EntityTransaction transaccion = em.getTransaction();

                            String nombre = request.getParameter("nombre");
                            String apellido = request.getParameter("apellido");
                            String mail = request.getParameter("mail");

                            Query consulta = null;
                            if (!nombre.equals("") || !apellido.equals(""))
                            {
                                if (!nombre.equals("") && !apellido.equals(""))
                                {
                                    consulta = em.createNamedQuery("Usuario.findByNombreAndApellido");
                                }
                                else
                                {
                                    consulta = em.createNamedQuery("Usuario.findByNombreOrApellido");
                                }
                                consulta.setParameter("nombre", nombre);
                                consulta.setParameter("apellido", apellido);
                                consulta.setParameter("id", usuarioActual.getId());
                            }
                            else
                            {
                                consulta = em.createNamedQuery("Usuario.findByMail");
                                consulta.setParameter("id", usuarioActual.getId());
                                consulta.setParameter("mail", mail);
                            }

                            List<Usuario> usuarios = consulta.getResultList();
                            enviarJson(usuarios, request, out);

//ELIMINAR o INVITAR Amigos----------------------------------------------------------------------
                        }
                        else
                            if (accion.equals("eliminarAmigo") || accion.equals("invitarAmigo"))
                            {
                                try
                                {
                                    EntityTransaction transaccion = em.getTransaction();

                                    String[] ids = request.getParameter("ids").split("-");
                                    if (accion.equals("eliminarAmigo") && request.getParameter("idGrupo").equals("todos"))
                                    {
                                        Usuario usrAux = null;
                                        Query consulta = em.createNamedQuery("Grupo.findAllByIdUsuario");
                                        consulta.setParameter("idUsuario", usuarioActual.getId());
                                        List<Grupo> grupos = consulta.getResultList();
                                        for (Grupo grupo : grupos)
                                        {
                                            for (int i = 0; i < ids.length; i++)
                                            {
                                                usrAux = em.find(Usuario.class, Integer.valueOf(ids[i]));
                                                if (usrAux != null)
                                                {
                                                    grupo.getUsuarioCollection().remove(usrAux);
                                                    transaccion.begin();
                                                    em.merge(grupo);
                                                    transaccion.commit();
                                                }
                                            }
                                        }
                                    }
                                    else
                                    {
                                        Grupo grupo = em.find(Grupo.class, Integer.valueOf(request.getParameter("idGrupo")));

                                        for (int i = 0; i < ids.length; i++)
                                        {
                                            if (accion.equals("eliminarAmigo"))
                                            {
                                                grupo.getUsuarioCollection().remove(em.find(Usuario.class, Integer.valueOf(ids[i])));
                                            }
                                            else
                                            {
                                                 grupo.getUsuarioCollection().add(em.find(Usuario.class, Integer.valueOf(ids[i])));
                                                //rupo.agregarAmigo(em.find(Usuario.class, Integer.valueOf(ids[i])));
                                            }
                                        }
                                        transaccion.begin();
                                        em.merge(grupo);
                                        transaccion.commit();
                                    }
                                    out.write("{success:true}");
                                }
                                catch (NumberFormatException e)
                                {
                                    out.write("{failure:true,msg:\"error FORMATO DE NUMERO\"}");
                                }
                                catch (PersistenceException e)
                                {
                                    out.write("{failure:true,msg:\"El usuario Ya pertenece a este grupo\"}");
                                }
//-- CARGAR ARBOL DE GRUPOS-----------------------------------------------------------------------------
                            }
                            else
                                if (accion.equals("cargarArbolGrupos"))
                                {
                                    try
                                    {
                                        Query consulta = em.createNamedQuery("Grupo.findAllByIdUsuario");
                                        consulta.setParameter("idUsuario", usuarioActual.getId());
                                        List<Grupo> grupos = consulta.getResultList();
                                        int contador = 0;
                                        if (request.getParameter("invitar") != null)
                                        {
                                            out.write("{total:" + grupos.size() + ",grupos:");//Creamos Json Combo
                                        }
                                        out.write("[");
                                        for (Grupo grupo : grupos)
                                        {
                                            out.write("{id:" + grupo.getId()
                                                    + ",text:\"" + grupo.getNombre()
                                                    + "\",cls:\"grupo"
                                                    + "\",leaf:true}");
                                            contador++;
                                            if (contador != grupos.size())
                                            {
                                                out.write(",");
                                            }
                                        }
                                        out.write("]");
                                        if (request.getParameter("invitar") != null)
                                        {
                                            out.write("}");
                                        }
                                    }
                                    catch (NumberFormatException e)
                                    {
                                        out.write("{failure:true,msg:\"error FORMATO DE NUMERO\"}");
                                    }
                                } //--cargar fotos-----------------------------------------------------------------------------
                                else
                                    if (accion.equals("crearGrupo"))
                                    {
                                        try
                                        {
                                            Grupo newGrupo = new Grupo(request.getParameter("nombre"), request.getParameter("descripcion"));
                                            newGrupo.setUsuario(usuarioActual);
                                            EntityTransaction transaccion = em.getTransaction();

                                            transaccion.begin();
                                            em.persist(newGrupo);
                                            transaccion.commit();
                                            out.write("{success:true}");
                                        }
                                        catch (NoResultException e)
                                        {
                                            out.write("{failure:true,msg:\"error en la consulta de grupo\"}");
                                        }
                                        catch (NumberFormatException e)
                                        {
                                            out.write("{failure:true,msg:\"error FORMATO DE NUMERO\"}");
                                        }
                                    }
                                    else
                                        if (accion.equals("eliminarGrupo"))
                                        {
                                            try
                                            {
                                                EntityTransaction transaccion = em.getTransaction();
                                                String id = request.getParameter("id");
                                                Query consulta = em.createNamedQuery("Grupo.findById");
                                                consulta.setParameter("id", Integer.valueOf(id));

                                                Grupo grupo = (Grupo) consulta.getSingleResult();
                                                transaccion.begin();
                                                em.remove(grupo);
                                                transaccion.commit();
                                                out.write("{success:true,msg:\"El grupo se ha eliminado.\"}");
                                            }
                                            catch (NumberFormatException e)
                                            {
                                                out.write("{failure:true,msg:\"error FORMATO DE NUMERO\"}");
                                            }
                                            catch (PersistenceException e)
                                            {
                                                out.write("{failure:true,msg:\"Error de Persistencia\"}");
                                            }
                                        }
            }
        }
        finally
        {
            out.close();
        }
    }

    void enviarJson(List usuarios1, HttpServletRequest request, PrintWriter out)
    {

        List<Usuario> usuarios = usuarios1;
        //paginador--------------------------------------------------------
        if (request.getParameter("start") != null)
        {
            int start = Integer.valueOf(request.getParameter("start"));
            int limit = Integer.valueOf(request.getParameter("limit")) + start;
            if (limit > usuarios1.size())
            {
                limit = usuarios1.size();
            }
            usuarios = usuarios1.subList(start, limit);
        }
        //escribir el JSON--------------------------------------------------
        out.write("{amigos:[");
        int contador = 0;
        for (Usuario usuario : usuarios)
        {
            out.write("{id:" + usuario.getId()
                    + ",nombre:\"" + usuario.getNombre()
                    + "\",apellido:\"" + usuario.getApellido()
                    + "\",mail:\"" + usuario.getMail()
                    + "\"}");
            contador++;
            if (contador != usuarios.size())
            {
                out.write(",");
            }
        }
        out.write("]}");
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
