package gestionarMuro;

import gestionarPersistencia.Comentario;
import gestionarPersistencia.Muro;
import gestionarPersistencia.Usuario;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import persistencia.ConexionBD;

public class ServicioMuro extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {

//CONTROLO SESION-----------------------------------------------
            HttpSession sesion = request.getSession(true);
            Usuario usuarioActual = (Usuario) sesion.getAttribute("usuarioActual");
            Boolean sesionIniciada = (Boolean) sesion.getAttribute("sesionIniciada");
            System.out.println("UsuarioActual: " + usuarioActual.getId());
            if (sesionIniciada != true || usuarioActual.getId() == null) {
                response.sendRedirect("index.jsp");
            } else {
                String accion = request.getParameter("accion") == null ? "" : request.getParameter("accion");
                EntityManager em = ConexionBD.getInstancia().getEm();

//IR AL MURO----------------------------------------------------------------------
                if (accion.equals("muro")) {
                    response.sendRedirect("elmuro.jsp");

//Cargar Comentarios----------------------------------------------------------------------
                } else if (accion.equals("cargarComentario")) {

                    Query consulta = em.createNamedQuery("Muro.buscarTodosComentarios");
                    consulta.setParameter("usuarioActual", usuarioActual.getId());
                    List comentarios1 = consulta.getResultList();
                    List<Object[]> comentarios = comentarios1;
                    //paginador--------------------------------------------------------
                    if (request.getParameter("start") != null) {
                        int start = Integer.valueOf(request.getParameter("start"));
                        int limit = Integer.valueOf(request.getParameter("limit")) + start;
                        if (limit >= comentarios1.size()) {
                            limit = comentarios1.size();
                        }
                        comentarios = comentarios1.subList(start, limit);
                    }
                    //escribir el JSON--------------------------------------------------
                    out.write("{total:" + comentarios1.size() + ",comentarios:[");
                    int contador = 0;
                    for (Object[] comentario : comentarios) {
                        out.write("{id:" + comentario[0]
                                + ",fecha:\"" + ((Date) comentario[1]).toGMTString()
                                + "\",descripcion:\"" + comentario[2]
                                + "\",usuario:\"" + comentario[3]
                                + " " + comentario[4] + "\"}");
                        contador++;
                        if (contador != comentarios.size()) {
                            out.write(",");
                        }
                    }
                    out.write("]}");

//NUEVO Comentario----------------------------------------------------------------------
                } else if (accion.equals("comentar")) {
                    try {
                        EntityTransaction transaccion = em.getTransaction();

                        Comentario comentarioNuevo = new Comentario(request.getParameter("comentario"), new Date());

                        Usuario usuarioInvitado = (Usuario) sesion.getAttribute("usuarioInvitado");
                        comentarioNuevo.setUsuario(usuarioInvitado);
                        transaccion.begin();
                        em.persist(comentarioNuevo);
                        transaccion.commit();
                        em.refresh(comentarioNuevo);
                        Muro muro = new Muro(comentarioNuevo, usuarioActual);
                        muro.setIdComentario(comentarioNuevo.getId());
                        transaccion.begin();
                        em.persist(muro);
                        transaccion.commit();
                        out.write("{success:true}");
                    } catch (NumberFormatException e) {
                        out.write("{failure:true,msg:\"error FORMATO DE NUMERO\"}");
                    }
//ELIMINAR Comentarios----------------------------------------------------------------------
                } else if (accion.equals("eliminarComentario")) {
                    try {
                        EntityTransaction transaccion = em.getTransaction();
                        Query consulta = em.createNamedQuery("Comentario.findById");
                        String[] ids = request.getParameter("ids").split("-");
                        for (int i = 0; i < ids.length; i++) {
                            consulta.setParameter("id", Integer.valueOf(ids[i]));
                            transaccion.begin();
                            em.remove((Comentario) consulta.getSingleResult());
                            transaccion.commit();
                        }
                        out.write("{success:true}");
                    } catch (NumberFormatException e) {
                        out.write("{failure:true,msg:\"error FORMATO DE NUMERO\"}");
                    }
                }
            }
        } finally {
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
            throws ServletException, IOException {
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
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
