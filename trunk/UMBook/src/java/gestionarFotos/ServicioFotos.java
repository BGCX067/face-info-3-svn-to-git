package gestionarFotos;

import gestionarPersistencia.Album;
import gestionarPersistencia.Foto;
import gestionarPersistencia.Usuario;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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

public class ServicioFotos extends HttpServlet
{

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        ServletContext context = getServletContext();

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try
        {
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

//Cargar Album en un COMBOBOX----------------------------------------------------------------------
                if (accion.equals("cargarAlbums"))
                {
                    try
                    {
                        Query consulta = em.createNamedQuery("Album.findAllByIdUsuario");
                        consulta.setParameter("idUsuario", usuarioActual.getId());
                        List<Album> albums = consulta.getResultList();
                        enviarJson(albums, request, out);
                    }
                    catch (NoResultException e)
                    {
                        out.write("{failure:true,msg:\"error en la consulta de album\"}");
                    }
                    catch (NumberFormatException e)
                    {
                        out.write("{failure:true,msg:\"error FORMATO DE NUMERO\"}");
                    }
//--Cargar arbol de Album-----------------------------------------------------------------------------
                }
                else
                    if (accion.equals("cargarArbolAlbums"))
                    {
                        try
                        {
                            Query consulta = em.createNamedQuery("Album.findAllByIdUsuario");
                            consulta.setParameter("idUsuario", usuarioActual.getId());
                            List<Album> albums = consulta.getResultList();
                            int contador = 0;
                            out.write("[");
                            for (Album album : albums)
                            {
                                out.write("{id:" + album.getId()
                                        + ",text:\"" + album.getNombre()
                                        + "\",leaf:true}");
                                contador++;
                                if (contador != albums.size())
                                {
                                    out.write(",");
                                }
                            }
                            out.write("]");
                        }
                        catch (NumberFormatException e)
                        {
                            out.write("{failure:true,msg:\"error FORMATO DE NUMERO\"}");
                        }
//--BUSCA TODAS LAS FOTOS DE UN ALBUM----------------------------------------------------------------------
                    }
                    else
                        if (accion.equals("buscarFotos"))
                        {
                            try
                            {

                                Query consulta = em.createNamedQuery("Foto.findAllByIdAlbum");
                                consulta.setParameter("idAlbum", Integer.valueOf(request.getParameter("idAlbum")));

                                List<Foto> fotos = consulta.getResultList();
                                enviarJson(fotos, request, out);
                            }
                            catch (NoResultException e)
                            {
                                out.write("{failure:true,msg:\"error en la consulta de album\"}");
                            }
                            catch (NumberFormatException e)
                            {
                                out.write("{failure:true,msg:\"error FORMATO DE NUMERO\"}");
                            }
                            //--CREAR NUEVO ALBUM--------------------------------------------------------------------------------------
                        }
                        else
                            if (accion.equals("crearAlbum"))
                            {
                                try
                                {
                                    Album newAlbum = new Album(request.getParameter("nombre"), new Date());
                                    newAlbum.setDescripcion(request.getParameter("descripcion"));
                                    newAlbum.setUsuario(usuarioActual);
                                    EntityTransaction transaccion = em.getTransaction();

                                    transaccion.begin();
                                    em.persist(newAlbum);
                                    transaccion.commit();
                                    em.refresh(newAlbum);
                                    out.write("{success:true}");
                                    File album = new File(context.getInitParameter("directorioFotos") + usuarioActual.getId() + "/" + newAlbum.getId());
                                    album.mkdir();
                                }
                                catch (NoResultException e)
                                {
                                    out.write("{failure:true,msg:\"error en la consulta de album\"}");
                                }
                                catch (NumberFormatException e)
                                {
                                    out.write("{failure:true,msg:\"error FORMATO DE NUMERO\"}");
                                }
                                // ELIMINAR FOTO ---------------------------------------
                            }
                            else
                                if (accion.equals("eliminarFoto"))
                                {
                                    try
                                    {
                                        EntityTransaction transaccion = em.getTransaction();
                                        String[] ids = request.getParameter("ids").split("-");

                                        Query consulta = em.createNamedQuery("Foto.findById");
                                        for (String idFoto : ids)
                                        {
                                            consulta.setParameter("id", Integer.valueOf(idFoto));
                                            Foto foto = (Foto) consulta.getSingleResult();
                                            transaccion.begin();
                                            em.remove(foto);
                                            transaccion.commit();
                                        }
                                        out.write("{success:true,msg:\"Las fotos se eliminaron exitosamente.\"}");
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
                                else
                                    if (accion.equals("eliminarAlbum"))
                                    {
                                        try
                                        {
                                            EntityTransaction transaccion = em.getTransaction();
                                            String[] ids = request.getParameter("ids").split("-");

                                            Query consulta = em.createNamedQuery("Album.findById");
                                            for (String idFoto : ids)
                                            {
                                                consulta.setParameter("id", Integer.valueOf(idFoto));
                                                Album album = (Album) consulta.getSingleResult();
                                                transaccion.begin();
                                                em.remove(album);
                                                transaccion.commit();
                                            }
                                            out.write("{success:true,msg:\"El album se eliminó exitosamente.\"}");
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

    void enviarJson(List lista, HttpServletRequest request, PrintWriter out)
    {

        //escribir el JSON--------------------------------------------------
        out.write("{total:" + lista.size() + ",data:[");
        int contador = 0;
        for (Object objeto : lista)
        {
            if (objeto instanceof Album)
            {
                Album album = (Album) objeto;
                out.write("{id:" + album.getId()
                        + ",nombre:\"" + album.getNombre()
                        + "\",descripcion:\"" + album.getDescripcion()
                        + "\",fecha:\"" + album.getFecha()
                        + "\"}");
            }
            if (objeto instanceof Foto)
            {

                Foto foto = (Foto) objeto;
                out.write("{id:" + foto.getId()
                        + ",ubicacion:\"" + foto.getUbicacion()
                        + "\",descripcion:\"" + foto.getDescripcion()
                        + "\",idUsuario:\"" + ((Usuario) request.getSession(true).getAttribute("usuarioActual")).getId()
                        + "\",idAlbum:\"" + foto.getAlbum().getId()
                        + "\",descripcionAlbum:\"" + foto.getAlbum().getDescripcion()
                        + "\"}");
            }
            contador++;
            if (contador != lista.size())
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
