package apiAssignment;

import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ResponseBody;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.*;
public class ApiAssignment {

	@Test
	public void getTest() throws InterruptedException
	{
		ResponseBody responseBody=given()
			
		.when()
			.get("https://api.coingecko.com/api/v3/coins/bitcoin")
		.then()
			.statusCode(200)
			.extract().response().body();
		
		JsonPath jsonPath=new JsonPath(responseBody.asString());
		int tickersSize=jsonPath.getInt("tickers.size()");
		Thread.sleep(10);
		ArrayList<String> bpiList=new ArrayList<String>();
		for(int i=0;i<tickersSize;i++)
		{
			String targetBPIs=jsonPath.getString("tickers["+i+"].target");
			bpiList.add(targetBPIs);
			
		}
		Thread.sleep(10);
		assertThat(bpiList.contains("USD"), is(true));
		assertThat(bpiList.contains("GBP"), is(true));
		assertThat(bpiList.contains("EUR"), is(true));
		
		
		Map<Object,Object> marketCapData=jsonPath.getMap("market_data.market_cap");
		assertThat(marketCapData.size(), is(notNullValue()));
		
		Map<Object,Object> total_volumeMap=jsonPath.getMap("market_data.total_volume");
		assertThat(total_volumeMap.size(), is(notNullValue()));
		
		double priceChangePerc24Hr=jsonPath.getDouble("market_data.market_cap_change_24h");
		assertThat(priceChangePerc24Hr, is(notNullValue()));
	}

}
