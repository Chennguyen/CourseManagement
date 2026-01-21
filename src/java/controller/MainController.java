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

/**
 *
 * @author Lenovo
 */
public class MainController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    // Khai báo các trang và Controller đích
    private static final String LOGIN_PAGE = "login.jsp";

    // Action: Login
    private static final String LOGIN = "Login";
    private static final String LOGIN_CONTROLLER = "LoginController";

    private static final String CREATE = "Create";
    private static final String CREATE_CONTROLLER = "CreateController";

    // Action: Search
    private static final String SEARCH = "Search";
    private static final String SEARCH_CONTROLLER = "SearchController";

    // Action: Delete
    private static final String DELETE = "Delete";
    private static final String DELETE_CONTROLLER = "DeleteController";

    // Action: Update
    private static final String UPDATE = "Update";
    private static final String UPDATE_CONTROLLER = "UpdateController";

    // Action: Logout
    private static final String LOGOUT = "Logout";
    private static final String LOGOUT_CONTROLLER = "LogoutController";

    private static final String ADD_TO_CART = "AddToCart";
    private static final String ADD_TO_CART_CONTROLLER = "AddToCartController";

    private static final String VIEW_DETAIL = "ViewDetail";
    private static final String VIEW_DETAIL_CONTROLLER = "ViewDetailController";

    private static final String VIEW_CART = "ViewCart";
    private static final String VIEW_CART_CONTROLLER = "ViewCartController";

    private static final String REMOVE_FROM_CART = "Remove";
    private static final String REMOVE_FROM_CART_CONTROLLER = "RemoveFromCartController";

    private static final String CHECKOUT = "Checkout";
    private static final String CHECKOUT_CONTROLLER = "CheckoutController";

    private static final String SUBMIT_FEEDBACK = "SubmitFeedback";
    private static final String SUBMIT_FEEDBACK_CONTROLLER = "SubmitFeedbackController";

    private static final String DISCOUNT = "Discount";
    private static final String DISCOUNT_CONTROLLER = "DiscountController";

    private static final String UPDATE_CART = "UpdateCart";
    private static final String UPDATE_CART_CONTROLLER = "UpdateCartController";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // Mặc định url là trang Login
        String url = LOGIN_PAGE;

        try {
            String action = request.getParameter("action");

            if (action == null) {
                url = LOGIN_PAGE;
            } else if (LOGIN.equals(action)) {
                url = LOGIN_CONTROLLER;
            } else if (CREATE.equals(action)) {
                url = CREATE_CONTROLLER;
            } else if (SEARCH.equals(action)) {
                url = SEARCH_CONTROLLER;
            } else if (DELETE.equals(action)) {
                url = DELETE_CONTROLLER;
            } else if (UPDATE.equals(action)) {
                url = UPDATE_CONTROLLER;
            } else if (LOGOUT.equals(action)) {
                url = LOGOUT_CONTROLLER;
            } else if (ADD_TO_CART.equals(action)) {
                url = ADD_TO_CART_CONTROLLER;
            } else if (VIEW_CART.equals(action)) {
                url = VIEW_CART_CONTROLLER;
            } else if (UPDATE_CART.equals(action)) { // Route cho nút + -
                url = UPDATE_CART_CONTROLLER;
            } else if (REMOVE_FROM_CART.equals(action)) {
                url = REMOVE_FROM_CART_CONTROLLER;
            } else if (VIEW_DETAIL.equals(action)) {
                url = VIEW_DETAIL_CONTROLLER;
            } else if (CHECKOUT.equals(action)) {
                url = CHECKOUT_CONTROLLER;
            } else if (SUBMIT_FEEDBACK.equals(action)) {
                url = SUBMIT_FEEDBACK_CONTROLLER;
            } else if (DISCOUNT.equals(action)) {
                url = DISCOUNT_CONTROLLER;
            }
        } catch (Exception e) {
            log("Error at MainController: " + e.toString());
        } finally {
            // Forward request tới controller đích
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
