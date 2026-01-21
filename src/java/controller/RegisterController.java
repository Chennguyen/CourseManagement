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
import model.UserDAO;
import model.UserDTO;
import model.UserErrorDTO;
import org.apache.catalina.User;

/**
 *
 * @author Lenovo
 */
public class RegisterController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private static final String ERROR = "register.jsp";
    private static final String SUCCESS = "login.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = ERROR;
        
        // Gọi class UserError để hứng lỗi validation
        UserErrorDTO userError = new UserErrorDTO();
        boolean foundErr = false;
        
        try {
            String userID = request.getParameter("userID");
            String fullName = request.getParameter("fullName");
            String password = request.getParameter("password");
            String confirm = request.getParameter("confirm");
            
            // MẶC ĐỊNH LÀ USER (US) để bảo mật
            String roleID = "US"; 

            // 1. Validation (Kiểm tra dữ liệu đầu vào)
            // UserID từ 2 đến 50 ký tự (Khớp DB varchar(50))
            if (userID.length() < 2 || userID.length() > 50) { 
                userError.setUserID("User ID must be [2,50] chars");
                foundErr = true;
            }
            // FullName từ 5 đến 50 ký tự
            if (fullName.length() < 5 || fullName.length() > 50) {
                userError.setFullName("Full Name must be [5,50] chars");
                foundErr = true;
            }
            // Check Confirm Password
            if (!confirm.equals(password)) {
                userError.setConfirm("Confirm password does not match");
                foundErr = true;
            }

            if (foundErr) {
                // Nếu có lỗi validation -> Gửi lỗi về JSP để hiện dòng đỏ đỏ
                request.setAttribute("USER_ERROR", userError);
            } else {
                // 2. Check trùng ID dưới Database
                UserDAO dao = new UserDAO();
                boolean checkDuplicate = dao.checkDuplicate(userID); 
                
                if (checkDuplicate) {
                    userError.setUserID("User ID already exists: " + userID);
                    request.setAttribute("USER_ERROR", userError);
                } else {
                    // 3. Nếu mọi thứ ok -> Insert
                    // Thứ tự Constructor: userID, fullName, password, roleID (Khớp model UserDTO)
                    UserDTO user = new UserDTO(userID, fullName, password, roleID);
                    
                    // Gọi hàm insert trong UserDAO
                    boolean checkInsert = dao.insert(user); 
                    
                    if (checkInsert) {
                        url = SUCCESS;
                        request.setAttribute("MESSAGE", "Registered successfully! Please login.");
                    } else {
                         request.setAttribute("ERROR", "Unknown error! Please try again.");
                    }
                }
            }
        } catch (Exception e) {
            log("Error at RegisterController: " + e.toString());
            if (e.toString().contains("duplicate")) {
                userError.setUserID("User ID already exists (SQL Error)!");
                request.setAttribute("USER_ERROR", userError);
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
