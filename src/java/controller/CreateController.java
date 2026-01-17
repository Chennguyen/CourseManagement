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
import model.CourseDAO;
import model.CourseDTO;

/**
 *
 * @author Lenovo
 */
public class CreateController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private static final String ERROR = "CreateCourse.jsp";
    private static final String SUCCESS = "MainController?action=Search&search="; // Quay về danh sách

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = ERROR;
        try {
            // 1. Lấy dữ liệu từ Form
            String id = request.getParameter("id");
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            int duration = Integer.parseInt(request.getParameter("duration"));
            float fee = Float.parseFloat(request.getParameter("fee"));
            String lecturer = request.getParameter("lecturer");
            String roadmap = request.getParameter("roadmap");

            // 2. Tạo DTO
            CourseDTO course = new CourseDTO(id, name, description, duration, fee, lecturer, roadmap);
            
            // 3. Gọi DAO
            CourseDAO dao = new CourseDAO();
            boolean check = dao.insertCourse(course);
            
            if (check) {
                url = SUCCESS;
            }
        } catch (Exception e) {
            log("Error at CreateController: " + e.toString());
            // Nếu lỗi (thường là do trùng ID), gửi thông báo ra trang JSP
            if (e.toString().contains("PRIMARY KEY")) {
                request.setAttribute("ERROR", "Duplicate Course ID! Please choose another one.");
            } else {
                request.setAttribute("ERROR", "Error: " + e.getMessage());
            }
        } finally {
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
