package gestionarFotos;

import gestionarPersistencia.Album;
import gestionarPersistencia.Foto;
import gestionarPersistencia.Usuario;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import persistencia.ConexionBD;

public class SubirFotos extends HttpServlet
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
//--SUBE UNA FOTO AL SERVIDOR-----------------------------------------------------------------------------

                if (ServletFileUpload.isMultipartContent(request))
                {

                    EntityManager em = ConexionBD.getInstancia().getEm();

                    FileItemFactory factory = new DiskFileItemFactory();
                    ServletFileUpload upload = new ServletFileUpload(factory);

                    List parsedRequest = upload.parseRequest(request);
                    Iterator iter = parsedRequest.iterator();
                    Foto fotoPersist = new Foto();
                    FileItem fileItem = null;

                    while (iter.hasNext())
                    {
                        FileItem item = (FileItem) iter.next();
                        if (item.isFormField())
                        {
                            if (item.getFieldName().equals("descripcion"))
                            {
                                fotoPersist.setDescripcion(item.getString());
                            }
                            else
                                if (item.getFieldName().equals("idAlbum"))
                                {
                                    fotoPersist.setAlbum(em.find(Album.class, Integer.valueOf(item.getString())));
                                }
                        }
                        else
                        {
                            fileItem = item;
                        }
                    }
                    fotoPersist.setUbicacion(fileItem.getName());

                    File newFoto = new File(context.getInitParameter("directorioFotos") + usuarioActual.getId() + "/" + fotoPersist.getAlbum().getId() + "/" + fotoPersist.getUbicacion());
                    IOUtils.copy(fileItem.getInputStream(), new FileOutputStream(newFoto.toString()));

                    EntityTransaction transaccion = em.getTransaction();
                    transaccion.begin();
                    em.persist(fotoPersist);
                    transaccion.commit();
                    out.write("{success:true}");
                }
                else
                {
                    throw new IllegalStateException("El request debe ser del tipo multipart!");
                }


            }

        }
        catch (FileUploadException ex)
        {
            Logger.getLogger(SubirFotos.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (NoResultException e)
        {
            out.write("{failure:true,msg:\"error en la consulta de album\"}");
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
