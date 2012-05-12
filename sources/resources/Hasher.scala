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
	def приветствие() : String =
	{
		"Доступные алгоритмы: Whirlpool, SHA"
	}

	@GET 
	@Path("Whirlpool/{что}")
	def whirlpool(@DefaultValue("") @PathParam("что") что : String) : String =
	{
		Json.build(Map("что" -> что, "хѣш" -> Whirlpool.hash(что))).toString
	}

	@GET 
	@Path("SHA/{что}")
	def sha(@DefaultValue("") @PathParam("что") что : String) : String =
	{
		Json.build(Map("что" -> что, "хѣш" -> SHA.hash(что))).toString
	}
}