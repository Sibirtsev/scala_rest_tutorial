﻿import org.mortbay.jetty.Server
import org.mortbay.jetty.servlet.{Context, ServletHolder}

import tools._

object Main
{
	def main(args: Array[String]) 
	{
		// создаём Jetty
		val server = new Server(Some[Int](System.getProperty("port").toInt).getOrElse(8080))
		val root = new Context(server, "/", Context.SESSIONS)
		
		// в каком пакете искать наши Rest веб-сервисы
		val package_name : String = System.getProperty("package")
		
		// каждый класс ScalatraServlet из этого пакета (и подпакетов) "замапить" на свой путь.
		// либо путь определяется методом "path" этого класса, либо совпадает с путём пакета 
		// (web.test.path.Servlet из пакета web будет "замаплен" на "/test/path/*", если не существует метода web.test.path.Servlet.path())
		for (handler <- PackageScanner.getClasses(package_name) if handler.getGenericSuperclass() == classOf[org.scalatra.ScalatraServlet])
		{
			// создаём сервлет из этого класса
			val servlet = handler.newInstance().asInstanceOf[javax.servlet.Servlet]
			
			// на какой путь "замапим" сервлет
			try
			{
				// получаем это из метода path()
				val path = handler.getMethod("path").invoke(servlet).toString
				
				// "мапим" сервлет на этот путь в Jetty
				root.addServlet(new ServletHolder(servlet), path + "/*")
			}
			catch 
			{
				// не написан метод path() у сервлета
				case ioe: NoSuchMethodException => 
					throw new RuntimeException("Method path() not found in class " + handler.getPackage().getName() + "." + handler.getName())
			}
		}
		
		// запускаем Jetty
		server.start()
		server.join()
	}
}

