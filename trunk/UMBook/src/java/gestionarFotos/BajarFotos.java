package gestionarFotos;

import gestionarPersistencia.Foto;
import gestionarPersistencia.Usuario;
import java.io.FileInputStream;
import java.io.IOException;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.IOUtils;
import persistencia.ConexionBD;

public class BajarFotos extends HttpServlet
{


    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
            ServletContext context = getServletContext();

        ServletOutputStream out = response.getOutputStream();
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

                EntityManager em = ConexionBD.getInstancia().getEm();

                int idFoto = 0;
                if (!request.getParameter("idFoto").equals("perfil"))
                {
                    idFoto = Integer.valueOf(request.getParameter("idFoto"));

                    //busca el archivo con ese id
                    Foto fotoEnvio = null;

                    fotoEnvio = em.find(Foto.class, idFoto);

                    //se envia el contenido
                    IOUtils.copy(new FileInputStream(context.getInitParameter("directorioFotos") + usuarioActual.getId() + "/" + fotoEnvio.getAlbum().getId() + "/" + fotoEnvio.getUbicacion()), out);
                }
                else
                {
                    IOUtils.copy(new FileInputStream(context.getInitParameter("directorioFotos") + "perfil.png"), out);
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
