/**
 * ExternalInformationService class provides gathering of information from google calendar, 
 * weather forecast and external sensors. Among other things it implements REST calls and 
 * XML parsing for calendar information and weather forecasts. 
 */
package heatcontrol

import org.apache.http._
import scala.xml.XML
import java.io._

object ExternalInformationService {
  
  weatherURL: String = "http://api.worldweatheronline.com/free/v1/weather.ashx?q=Groningen&format=XML&num_of_days=1&key="
  
  def getWeatherInformation() {
	val weatherString: String = weatherCall(weatherURL)
	val weatherXML = XML.loadString(weatherString)
	val temp_c = (weatherXML \\ "current_condition" \ "temp_c") text
	println(temp_c)
	
	
  }
  
  def weatherCall(url:String): String = {
    val httpClient = new defaultHttpClient()
    val httpResponds = httpClient.execute(new httpGet(url))
    val entity = httpResponds.getEntity()
    val content = ""
    if (entity != null) {
      val stream = entity.getContent()
      content = io.Source.fromInputStream(stream).getLines.mkString
      stream.close
    }
    httpClient.getConnectionManager.shutdown()
    return content    
  }
}