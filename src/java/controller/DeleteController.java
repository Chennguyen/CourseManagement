/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.CourseDAO;
import model.UserDTO;

/**
 *
 * @author Lenovo
 */
public class DeleteController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private static final String ERROR = "MainController?action=Search";
    private static final String SUCCESS = "MainController?action=Search";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = ERROR;
        try {
            // 1. Check Session & Role (BẢO MẬT)
            HttpSession session = request.getSession(false);
            UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");
            
            if (loginUser != null && "AD".equals(loginUser.getRoleID())) {
                // 2. Nếu là Admin thì mới cho xóa
                String id = request.getParameter("id");
                CourseDAO dao = new CourseDAO();
                boolean check = dao.deleteCourse(id);
                if (check) {
                    url = SUCCESS;
                }
            } else {
                // User thường cố tình truy cập -> Chặn
                request.setAttribute("ERROR", "You do not have permission to delete!");
            }
        } catch (Exception e) {
            log("Error at DeleteController: " + e.toString());
        } finally {
            // Dùng forward để giữ param search nếu cần, hoặc sendRedirect để refresh sạch
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
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
     *
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
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
