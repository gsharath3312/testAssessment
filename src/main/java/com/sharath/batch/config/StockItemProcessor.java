package com.sharath.batch.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.sharath.batch.model.Stock;

public class StockItemProcessor implements ItemProcessor<Stock, Stock> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StockItemProcessor.class);

    @Override
    public Stock process(final Stock stock) throws Exception {
    	//TODO::process stock item as per requirement
        LOGGER.info("Converting ( {} ) into ( {} )", stock, stock);

        return stock;
    }

}