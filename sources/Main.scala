import org.mortbay.jetty.Server
import org.mortbay.jetty.servlet.{Context, ServletHolder}

import tools._

object Main
{
	def main(args: Array[String]) 
	{
		// ������ Jetty
		val server = new Server(Some[Int](System.getProperty("port").toInt).getOrElse(8080))
		val root = new Context(server, "/", Context.SESSIONS)
		
		// � ����� ������ ������ ���� Rest ���-�������
		val package_name : String = System.getProperty("package")
		
		// ������ ����� ScalatraServlet �� ����� ������ (� ����������) "��������" �� ���� ����.
		// ���� ���� ������������ ������� "path" ����� ������, ���� ��������� � ���� ������ 
		// (web.test.path.Servlet �� ������ web ����� "��������" �� "/test/path/*", ���� �� ���������� ������ web.test.path.Servlet.path())
		for (handler <- PackageScanner.getClasses(package_name) if handler.getGenericSuperclass() == classOf[org.scalatra.ScalatraServlet])
		{
			// ��������� �������� ��������� ����� ������
			var subpackage = handler.getPackage().getName()
			subpackage = subpackage.substring(package_name.length)
			if (subpackage.startsWith("."))
				subpackage = subpackage.substring(1)
				
			// ������ ������� �� ����� ������
			val servlet = handler.newInstance().asInstanceOf[javax.servlet.Servlet]
			
			// �� ����� ���� "�������" �������
			var path = ""
			
			try
			{
				// ���� �� ������ path()
				path = handler.getMethod("path").invoke(servlet).toString
			}
			catch 
			{
				// ���� �� �������� ���������
				case ioe: NoSuchMethodException => 
					path = subpackage.replaceAll(".", "/")
			}
			
			// "�����" ������� �� ���� ���� � Jetty
			root.addServlet(new ServletHolder(servlet), path + "/*")
		}
		
		// ��������� Jetty
		server.start()
		server.join()
	}
}

