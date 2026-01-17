/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Cart;
import model.DiscountDAO;
import model.DiscountDTO;

/**
 *
 * @author Lenovo
 */
public class DiscountController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = "ViewCart.jsp";
        try {
            String code = request.getParameter("discountCode");
            HttpSession session = request.getSession();
            Cart cart = (Cart) session.getAttribute("CART");
            
            if (cart == null) {
                cart = new Cart();
                session.setAttribute("CART", cart);
            }

            DiscountDAO dao = new DiscountDAO();
            DiscountDTO dto = dao.getDiscount(code);

            if (dto == null) {
                request.setAttribute("DISCOUNT_ERROR", "Code does not exist!");
            } else {
                // Check hạn sử dụng
                Date now = new Date(System.currentTimeMillis());
                if (dto.getExpiryDate().before(now)) {
                    request.setAttribute("DISCOUNT_ERROR", "Code is expired!");
                } 
                // Check số lượng
                else if (dto.getQuantity() <= 0) {
                    request.setAttribute("DISCOUNT_ERROR", "Code is sold out!");
                } 
                // OK -> Áp dụng
                else {
                    cart.setDiscountPercent(dto.getPercentDiscount());
                    session.setAttribute("CART", cart); // Update lại session
                    request.setAttribute("DISCOUNT_MSG", "Applied code: " + code + " (-" + dto.getPercentDiscount() + "%)");
                }
            }
        } catch (Exception e) {
            log("Error at DiscountController: " + e.toString());
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
