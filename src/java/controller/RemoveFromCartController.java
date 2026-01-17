package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Cart;
import model.CartDAO; // <--- Import
import model.UserDTO; // <--- Import

public class RemoveFromCartController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            Cart cart = (Cart) session.getAttribute("CART");
            String id = request.getParameter("id");
            
            if (cart != null) {
                // Logic cũ: Xóa khỏi Session
                boolean check = cart.remove(id);
                
                if (check) {
                    session.setAttribute("CART", cart);
                    
                    // --- MỚI THÊM: XÓA KHỎI DB ---
                    UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");
                    if (user != null) {
                        CartDAO cartDAO = new CartDAO();
                        cartDAO.removeFromCart(user.getUserID(), id);
                    }
                    // -----------------------------
                }
            }
        } catch (Exception e) {
            log("Error at RemoveFromCartController: " + e.toString());
        }
        response.sendRedirect("MainController?action=ViewCart");
    }

    @Override protected void doGet(HttpServletRequest r, HttpServletResponse p) throws ServletException, IOException {processRequest(r,p);}
    @Override protected void doPost(HttpServletRequest r, HttpServletResponse p) throws ServletException, IOException {processRequest(r,p);}
}