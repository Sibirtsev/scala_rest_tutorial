import org.mortbay.jetty.Server
import org.mortbay.jetty.servlet.{Context, ServletHolder}

import tools._

object Main
{
	def main(args: Array[String]) 
	{
		val server = new Server(Some[Int](System.getProperty("port").toInt).getOrElse(8080))
		val root = new Context(server, "/", Context.SESSIONS)
		
		val package_name : String = System.getProperty("package")
		for (handler <- PackageScanner.getClasses(package_name) if handler.getGenericSuperclass() == classOf[org.scalatra.ScalatraServlet])
		{
			var subpackage = handler.getPackage().getName()
			subpackage = subpackage.substring(package_name.length)
			if (subpackage.startsWith("."))
				subpackage = subpackage.substring(1)
				
			val servlet = handler.newInstance().asInstanceOf[javax.servlet.Servlet]
			
			var path = ""
			
			try
			{
				path = handler.getMethod("path").invoke(servlet).toString
			}
			catch 
			{
				case ioe: NoSuchMethodException => 
					path = subpackage.replaceAll(".", "/")
			}
			
			root.addServlet(new ServletHolder(servlet), path + "/*")
		}
		
		server.start()
		server.join()
	}
}

