import  java.io.IOException;
import  java.util.Properties;
import  javax.servlet.ServletContext;
import  javax.servlet.ServletException;
import  javax.servlet.http.HttpServlet;
import  javax.servlet.http.HttpServletRequest;
import  javax.servlet.http.HttpServletResponse;
import  javax.servlet.annotation.WebServlet;
@WebServlet("/MailServlet")
public  class  MailServlet  extends  HttpServlet  {
    @Override
    protected  void  doPost  (HttpServletRequest  request,
                              HttpServletResponse  response)  throws  ServletException,  IOException  {
        //   запрос  параметров  почтового  серберп  из  maiL.properties
        Properties  properties  =  new  Properties();
        ServletContext context = getServletContext();
        String filename="";
        try {
            filename = context.getInitParameter("mail");
        } catch(Exception e){
            System.out.println("Загрузка параметров не удалась "+e.getMessage());
        }
// загрузка поромеироб почтового сервера в объект свойств
        properties.load(context.getResourceAsStream(filename));
        MailThread mailOperator =
                new MailThread(request.getParameter("to"),
                        request.getParameter("subject"),
                        request.getParameter("body"),
                        properties);
// запуск процесса отправки письма 6 отдельном потоке
        mailOperator.start();
// переход на страницу с предложение/! о создании нового письма
        request.getRequestDispatcher("send.jsp").forward(request,  response);
    }
}

