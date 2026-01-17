/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.CourseDAO;
import model.CourseDTO;
import model.FeedbackDTO;
import model.OrderDAO;
import model.UserDTO;

/**
 *
 * @author Lenovo
 */
public class ViewDetailController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private static final String DETAIL_PAGE = "CourseDetail.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = DETAIL_PAGE;
        try {
            String id = request.getParameter("id"); // Lấy ID khóa học (Ví dụ: C-001)
            CourseDAO dao = new CourseDAO();
            
            // 1. Lấy thông tin khóa học & Feedback
            CourseDTO course = dao.getCourseByID(id);
            request.setAttribute("COURSE_DETAIL", course);
            List<FeedbackDTO> feedbackList = dao.getFeedbacks(id);
            request.setAttribute("FEEDBACK_LIST", feedbackList);
            
            // ==================================================================
            // 2. LOGIC CHECK MUA HÀNG (ĐÃ SỬA ĐỂ DỄ CHỊU HƠN)
            // ==================================================================
            boolean canReview = false;
            HttpSession session = request.getSession();
            UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");
            
            // DEBUG: In ra cửa sổ Output của NetBeans để xem nó chạy tới đâu
            System.out.println("--- CHECK REVIEW PERMISSION ---");
            System.out.println("Course ID: " + id);
            
            if (user != null) {
                System.out.println("User ID: " + user.getUserID());
                System.out.println("User Role: " + user.getRoleID());
                
                // GỌI DAO CHECK
                OrderDAO orderDAO = new OrderDAO();
                boolean hasBought = orderDAO.hasUserBoughtCourse(user.getUserID(), id);
                
                System.out.println("Has Bought (From DB): " + hasBought);
                
                // Logic: Chỉ cần đã mua là cho Review (Bất chấp Role là Admin hay User)
                if (hasBought) {
                    canReview = true;
                }
            } else {
                System.out.println("User is NULL (Not logged in)");
            }
            System.out.println("=> FINAL DECISION (canReview): " + canReview);
            System.out.println("-------------------------------");
            
            request.setAttribute("CAN_REVIEW", canReview); 
            // ==================================================================
            
        } catch (Exception e) {
            log("Error: " + e.toString());
            e.printStackTrace();
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
