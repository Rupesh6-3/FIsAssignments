package apiAssignment;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.ArrayList;
import java.util.Map;

import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ResponseBody;
public class ApiAssignment {
	/***************************************************************************************
	1.Send the GET request
	****************************************************************************************/
	@Test
	public void getTest() throws InterruptedException
	{
		ResponseBody responseBody=given()
			
		.when()
			.get("https://api.coingecko.com/api/v3/coins/bitcoin")
		.then()
			.statusCode(200)
			.extract().response().body();
		
		/***************************************************************************************
		 2.Verify the response contains
		 a.There are 3 BPIs i. USD ii. GBP iii. EUR
		 ****************************************************************************************/
		JsonPath jsonPath=new JsonPath(responseBody.asString());
		int tickersSize=jsonPath.getInt("tickers.size()");
		Thread.sleep(10);
		ArrayList<String> bpiList=new ArrayList<String>();
		for(int i=0;i<tickersSize;i++)
		{
			String targetBPIs=jsonPath.getString("tickers["+i+"].target");
			bpiList.add(targetBPIs);
			
		}
		System.out.println(bpiList);
		Thread.sleep(20);
		assertThat(bpiList.contains("USD"), is(true));
		assertThat(bpiList.contains("GBP"), is(true));
		assertThat(bpiList.contains("EUR"), is(true));
		/***************************************************************************************
		 b.	Each cryptocurrency has a  market cap, and total volume.
		 ****************************************************************************************/
		Map<Object,Object> marketCapData=jsonPath.getMap("market_data.market_cap");
		assertThat(marketCapData.size(), is(notNullValue()));
		
		Map<Object,Object> total_volumeMap=jsonPath.getMap("market_data.total_volume");
		assertThat(total_volumeMap.size(), is(notNullValue()));
		
		/***************************************************************************************
		 c.The price change percentage over the last 24 hours
		 ****************************************************************************************/
		double priceChangePerc24Hr=jsonPath.getDouble("market_data.market_cap_change_24h");
		assertThat(priceChangePerc24Hr, is(notNullValue()));
		
		/***************************************************************************************
		 d.Verify homepage URL is not empty (in the interview)
		 ****************************************************************************************/
		int homepageSize=jsonPath.getInt("links.homepage.size()");
		String homePageUrl=jsonPath.getString("links.homepage[0]");
		assertThat(homePageUrl,is(notNullValue()));
		assertThat(homepageSize,is(notNullValue()));
	}

}
