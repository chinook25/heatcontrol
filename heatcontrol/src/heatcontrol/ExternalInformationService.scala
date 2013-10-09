/**
 * ExternalInformationService class provides gathering of information from google calendar, 
 * weather forecast and external sensors. Among other things it implements REST calls and 
 * XML parsing for calendar information and weather forecasts. 
 */
package heatcontrol

/* general imports for scala useages */
import scala.xml.XML
import scala.Console._

/* imports for http rest functions and calls */
import org.apache.http.client._
import org.apache.http.impl.client._
import org.apache.http.client.methods._

object ExternalInformationService {

  val weatherURL: String = "http://api.worldweatheronline.com/free/v1/weather.ashx?q=Groningen&format=XML&num_of_days=1&key=8phfymv55kgpmynugrzaq84v"
  
  def getWeatherInformation() {
	val weatherString: String = weatherCall(weatherURL)
	val weatherXML = XML.loadString(weatherString)
	val temp_c : Integer = Integer.parseInt((weatherXML \\ "temp_c").text)
	println("temp: " + temp_c)	
  }
  
  def weatherCall(url:String): String = {
    val httpClient = new DefaultHttpClient()
    val httpResponds = httpClient.execute(new HttpGet(url))
    val entity = httpResponds.getEntity()
    val content = ""
    if (entity != null) {
      val stream = entity.getContent()
      val content = io.Source.fromInputStream(stream).getLines.mkString
      stream.close
    }
    httpClient.getConnectionManager.shutdown()
    return content    
  }
}