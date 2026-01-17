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
import model.CourseDTO;
import model.UserDTO;

/**
 *
 * @author Lenovo
 */
public class UpdateController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private static final String UPDATE_PAGE = "updateCourse.jsp";
    private static final String SUCCESS = "MainController?action=Search"; // Update xong quay lại list

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = UPDATE_PAGE;
        
        try {
            // 1. Check Role ADMIN
            HttpSession session = request.getSession(false);
            UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");
            if (loginUser == null || !"AD".equals(loginUser.getRoleID())) {
                response.sendRedirect("login.jsp");
                return;
            }

            String actionType = request.getParameter("actionType");

            CourseDAO dao = new CourseDAO();

            // TRƯỜNG HỢP 1: Click link Update (Load dữ liệu lên form)
            // Khi click link, ta chưa gửi actionType, hoặc ta check method GET
            if (actionType == null) {
                String id = request.getParameter("id");
                CourseDTO course = dao.getCourseByID(id);
                request.setAttribute("COURSE_DETAIL", course);
                url = UPDATE_PAGE;
            } 
            // TRƯỜNG HỢP 2: Nhấn nút Save trên form (Lưu dữ liệu)
            else if ("Save".equals(actionType)) {
                String id = request.getParameter("id");
                String name = request.getParameter("name");
                String description = request.getParameter("description");
                int duration = Integer.parseInt(request.getParameter("duration"));
                float fee = Float.parseFloat(request.getParameter("fee"));

                String lecturer = request.getParameter("lecturer");
                String roadmap = request.getParameter("roadmap");

                // Tạo CourseDTO với Constructor mới (7 tham số)
                CourseDTO course = new CourseDTO(id, name, description, duration, fee, lecturer, roadmap);
                boolean check = dao.updateCourse(course);

                if (check) {
                    url = SUCCESS; // Thành công -> Quay về list
                    response.sendRedirect(url); // Redirect để tránh lỗi F5 resubmit
                    return;
                } else {
                    request.setAttribute("ERROR", "Update failed!");
                }
            }

        } catch (Exception e) {
            log("Error at UpdateController: " + e.toString());
        }
        request.getRequestDispatcher(url).forward(request, response);
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
