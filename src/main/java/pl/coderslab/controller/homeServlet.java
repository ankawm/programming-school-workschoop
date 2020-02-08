package pl.coderslab.controller;

import pl.coderslab.model.SolutionDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/")
public class homeServlet extends HttpServlet {

    private final SolutionDao solutionDao = new SolutionDao();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String sizeParam = getServletContext().getInitParameter("number-solutions");

        try{
            int size = Integer.parseInt(sizeParam);
            resp.getWriter().println(solutionDao.findRecent(size));
        }catch(NumberFormatException e) {
            resp.getWriter().println("Parametr nie jest liczba!!!");
        }

    }
}
