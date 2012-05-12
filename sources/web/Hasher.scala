package web

import org.scalatra._

import hash.Whirlpool
import hash.SHA

import com.twitter.json.Json

class Hasher extends ScalatraServlet 
{
	def path = "/захѣшировать"
	
	get("/")
	{
		"Доступные алгоритмы: Whirlpool, SHA"
	}

	get("/Whirlpool/:what") 
	{
		val что = params("what")
		Json.build(Map("что" -> что, "хѣш" -> Whirlpool.hash(что))).toString
	}

	get("/SHA/:what") 
	{
		val что = params("what")
		Json.build(Map("что" -> что, "хѣш" -> SHA.hash(что))).toString
	}
}