package resources

import javax.ws.rs._
import javax.ws.rs.core._

import hash.Whirlpool
import hash.SHA

import com.twitter.json.Json

@Path("/захѣшировать")
class Hasher
{	
	@GET 
	@Produces(Array("text/plain"))
	def приветствие() : String =
	{
		"Доступные алгоритмы: Whirlpool, SHA"
	}

	@GET 
	@Path("Whirlpool/{что}")
	@Produces(Array(MediaType.APPLICATION_JSON))
	def whirlpool(@DefaultValue("") @PathParam("что") что : String) : String =
	{
		if (что == "")
			throw new IllegalArgumentException("Что захешировать?")
	
		Json.build(Map("hash" -> Whirlpool.hash(что))).toString
	}

	@GET 
	@Path("SHA/{что}")
	@Produces(Array(MediaType.APPLICATION_JSON))
	def sha(@DefaultValue("") @PathParam("что") что : String) : String =
	{
		if (что == "")
			throw new IllegalArgumentException("Что захешировать?")
	
		Json.build(Map("hash" -> SHA.hash(что))).toString
	}
}