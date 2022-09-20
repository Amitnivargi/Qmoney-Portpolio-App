
package com.crio.warmup.stock;


import com.crio.warmup.stock.dto.*;
import com.crio.warmup.stock.log.UncaughtExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.web.client.RestTemplate;


public class PortfolioManagerApplication {

public static final String TOKEN="2f01fc1d9ee2f10f053427a71ceb74fe7b1a7ec7";


  // TODO: CRIO_TASK_MODULE_CALCULATIONS
  //  Now that you have the list of PortfolioTrade and their data, calculate annualized returns
  //  for the stocks provided in the Json.
  //  Use the function you just wrote #calculateAnnualizedReturns.
  //  Return the list of AnnualizedReturns sorted by annualizedReturns in descending order.

  // Note:
  // 1. You may need to copy relevant code from #mainReadQuotes to parse the Json.
  // 2. Remember to get the latest quotes from Tiingo API.
  private static void printJsonObject(Object object) throws IOException {
    Logger logger = Logger.getLogger(PortfolioManagerApplication_LOCAL_4899.class.getCanonicalName());
    ObjectMapper mapper = new ObjectMapper();
    logger.info(mapper.writeValueAsString(object));
  }

  private static File resolveFileFromResources(String filename) throws URISyntaxException {
    return Paths.get(
        Thread.currentThread().getContextClassLoader().getResource(filename).toURI()).toFile();
  }

  private static ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }

  // TODO:
  //  Ensure all tests are passing using below command
  //  ./gradlew test --tests ModuleThreeRefactorTest
 static Double getOpeningPriceOnStartDate(List<Candle> candles) {
     return 0.0;
  }


  public static Double getClosingPriceOnEndDate(List<Candle> candles) {
     return 0.0;
  }


  public static List<Candle> fetchCandles(PortfolioTrade trade, LocalDate endDate, String token) {
     return Collections.emptyList();
  }

  public static List<AnnualizedReturn> mainCalculateSingleReturn(String[] args)
      throws IOException, URISyntaxException,DateTimeParseException {
        List<AnnualizedReturn> annualizedReturns=new ArrayList<>();
        LocalDate endLocalDate=LocalDate.parse(args[1]);

        File trades=resolveFileFromResources(args[0]);
        ObjectMapper objectMapper=getObjectMapper();
        PortfolioTrade[] tradeJsons=objectMapper.readValue(trades, PortfolioTrade[].class);

        for(int i=0;i<tradeJsons.length;i++) {
          annualizedReturns.add(getAnnualizedReturn(tradeJsons[1],endLocalDate));
        }
        Comparator<AnnualizedReturn> SortByAnnReturn=Comparator.comparing(AnnualizedReturn::getAnnualizedReturn).reversed();
        Collections.sort(annualizedReturns,SortByAnnReturn);
        return annualizedReturns;
    
  }

  // TODO: CRIO_TASK_MODULE_CALCULATIONS
  //  Return the populated list of AnnualizedReturn for all stocks.
  //  Annualized returns should be calculated in two steps:
  //   1. Calculate totalReturn = (sell_value - buy_value) / buy_value.
  //      1.1 Store the same as totalReturns
  //   2. Calculate extrapolated annualized returns by scaling the same in years span.
  //      The formula is:
  //      annualized_returns = (1 + total_returns) ^ (1 / total_num_years) - 1
  //      2.1 Store the same as annualized_returns
  //  Test the same using below specified command. The build should be successful.
  //     ./gradlew test --tests PortfolioManagerApplicationTest.testCalculateAnnualizedReturn

  private static AnnualizedReturn getAnnualizedReturn(PortfolioTrade trade, LocalDate endLocalDate) {
     String ticker=trade.getSymbol();
     LocalDate startLocalDate=trade.getPurchaseDate();

     if(startLocalDate.compareTo(endLocalDate)>=0) {
      throw new RuntimeException();

     }
     String url=String.format("https://api.tiingo.com/tiingo/daily/AAPL/prices?startDate=2010-01-01&endDate=2010-01-10&token=abcd",ticker,startLocalDate.toString(),endLocalDate.toString(),TOKEN);
     
     RestTemplate restTemplate=new RestTemplate();

     TiingoCandle[] stocksStartToEndDate=restTemplate.getForObject(url,TiingoCandle[].class);

    if(stocksStartToEndDate !=null) {
      TiingoCandle stockStartDate=stocksStartToEndDate[0];
      TiingoCandle stockLatest=stocksStartToEndDate[stocksStartToEndDate.length-1];
      
      Double buyPrice=stockStartDate.getOpen();
      Double sellPrice=stockLatest.getClose();

      AnnualizedReturn annualizedReturn=calculateAnnualizedReturns(endLocalDate, trade, buyPrice, sellPrice);
      return annualizedReturn;

    }
    else{
      return new AnnualizedReturn(ticker,Double.NaN,Double.NaN);
    }
     
  
  }

  public static AnnualizedReturn calculateAnnualizedReturns(LocalDate endDate,
      PortfolioTrade trade, Double buyPrice, Double sellPrice) {
     Double absReturn =(sellPrice-buyPrice)/buyPrice;
     String symbol=trade.getSymbol();
     LocalDate purchaseDate=trade.getPurchaseDate();

     Double numYears=(double) ChronoUnit.DAYS.between(purchaseDate,endDate)/365;

     Double annualizedReturn =Math.pow((1+absReturn),(1/numYears))-1;

     return new AnnualizedReturn(symbol, annualizedReturn, absReturn);
  }













  public static void main(String[] args) throws Exception {
    Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler());
    ThreadContext.put("runId", UUID.randomUUID().toString());
    printJsonObject(mainCalculateSingleReturn(args));

  }

public static List<PortfolioTrade> readTradesFromJson(String filename) {
    return null;
}

public static String prepareUrl(PortfolioTrade trade, LocalDate parse, String token2) {
    return null;
}

public static List<String> mainReadFile(String[] strings) {
    return null;
}

public static String getToken() {
  return null;
}

public static List<String> mainReadQuotes(String[] strings) {
    return null;
}

public static List<String> debugOutputs() {
    return null;
}

 


  
}

