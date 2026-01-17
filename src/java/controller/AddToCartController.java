package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Cart;
import model.CartDAO; // <--- Import
import model.CourseDAO;
import model.CourseDTO;
import model.UserDTO; // <--- Import

public class AddToCartController extends HttpServlet {

    private static final String SUCCESS = "MainController?action=Search"; 

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = SUCCESS;
        try {
            String courseID = request.getParameter("id");
            CourseDAO dao = new CourseDAO();
            CourseDTO course = dao.getCourseByID(courseID); 
            
            if (course != null) {
                HttpSession session = request.getSession();
                Cart cart = (Cart) session.getAttribute("CART");
                
                if (cart == null) {
                    cart = new Cart();
                }
                
                // Logic cũ: Thêm vào Session
                boolean check = cart.add(course); // cart.add trả về true nếu thêm mới, false nếu trùng
                
                if (check) { // Chỉ lưu DB nếu là món mới
                    session.setAttribute("CART", cart);
                    
                    // --- MỚI THÊM: LƯU XUỐNG DB ---
                    UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");
                    if (user != null) {
                        CartDAO cartDAO = new CartDAO();
                        cartDAO.addToCart(user.getUserID(), courseID);
                    }
                    // ------------------------------
                    
                    session.setAttribute("MESSAGE", "Added " + course.getName() + " successfully!");
                } else {
                     session.setAttribute("MESSAGE", "Course already in cart!");
                }
                
                String lastSearch = request.getParameter("lastSearch");
                if(lastSearch == null) lastSearch = "";
                url = "MainController?action=Search&search=" + lastSearch;
            }
            
        } catch (Exception e) {
            log("Error at AddToCartController: " + e.toString());
        } finally {
            response.sendRedirect(url);
        }
    }

    @Override protected void doGet(HttpServletRequest r, HttpServletResponse p) throws ServletException, IOException {processRequest(r,p);}
    @Override protected void doPost(HttpServletRequest r, HttpServletResponse p) throws ServletException, IOException {processRequest(r,p);}
}