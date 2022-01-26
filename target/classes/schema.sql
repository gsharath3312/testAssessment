DROP TABLE stock IF EXISTS;

CREATE TABLE stock  (
    id BIGINT IDENTITY NOT NULL PRIMARY KEY,
	quarter VARCHAR(50),
    stock VARCHAR(50),
	date VARCHAR(50),
	open VARCHAR(50),
	high VARCHAR(50),
	low VARCHAR(50),
   close VARCHAR(50),
   volume VARCHAR(50),
   percent_change_price VARCHAR(50),
   percent_change_volume_over_last_wk VARCHAR(50),
   previous_weeks_volume VARCHAR(50),
   next_weeks_open VARCHAR(50),
   next_weeks_close VARCHAR(50),
   percent_change_next_weeks_price VARCHAR(50),
   days_to_next_dividend VARCHAR(50),
   percent_return_next_dividend VARCHAR(50)
);

