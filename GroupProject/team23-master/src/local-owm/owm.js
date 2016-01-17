var express = require('express');
var app = express();

app.get('/data/2.5/find', function(req, res) {
	res.json(find_response);
})

app.get('/data/2.5/weather', function(req, res) {
	res.json(weather_response);
})

app.get('/data/2.5/forecast/daily', function(req, res) {
	res.json(dailyforecast_response);
})

app.get('/data/2.5/forecast', function(req, res) {
	res.json(forecast_response);
})

app.get('/', function(req, res) {
	res.json({
		"end-points": [
			"/data/2.5/find/*",
			"/data/2.5/weather/*",
			"/data/2.5/forecast/*",
			"/data/2.5/forecast/daily/*"
		]
	});
});

app.listen(process.env.PORT || 4730);

var find_response = {
  "message": "like",
  "cod": "200",
  "count": 1,
  "list": [
    {
      "id": 6058560,
      "name": "London",
      "coord": {
        "lon": -81.23304,
        "lat": 42.983391
      },
      "main": {
        "temp": 277.58,
        "temp_min": 277.58,
        "temp_max": 277.58,
        "pressure": 1007.16,
        "sea_level": 1031.93,
        "grnd_level": 1007.16,
        "humidity": 94
      },
      "dt": 1426309849,
      "wind": {
        "speed": 6.16,
        "deg": 193.501
      },
      "sys": {
        "country": "CA"
      },
      "rain": {
        "3h": 2.04
      },
      "clouds": {
        "all": 100
      },
      "weather": [
        {
          "id": 500,
          "main": "Rain",
          "description": "light rain",
          "icon": "10n"
        }
      ]
    }
  ]
};

var weather_response = {
  "coord": {
    "lon": -81.23,
    "lat": 42.98
  },
  "sys": {
    "message": 0.0042,
    "country": "CA",
    "sunrise": 1426333066,
    "sunset": 1426375814
  },
  "weather": [
    {
      "id": 803,
      "main": "Clouds",
      "description": "broken clouds",
      "icon": "04n"
    }
  ],
  "base": "cmc stations",
  "main": {
    "temp": 4.981,
    "temp_min": 4.981,
    "temp_max": 4.981,
    "pressure": 1002.07,
    "sea_level": 1026.73,
    "grnd_level": 1002.07,
    "humidity": 81
  },
  "wind": {
    "speed": 7.36,
    "deg": 305.502
  },
  "clouds": {
    "all": 64
  },
  "dt": 1426377224,
  "id": 6058560,
  "name": "London",
  "cod": 200
};

var forecast_response = {
  "cod": "200",
  "message": 0.0878,
  "city": {
    "id": 6058560,
    "name": "London",
    "coord": {
      "lon": -81.23304,
      "lat": 42.983391
    },
    "country": "CA",
    "population": 346765
  },
  "cnt": 41,
  "list": [
    {
      "dt": 1426366800,
      "main": {
        "temp": 3.08,
        "temp_min": 3.08,
        "temp_max": 6.19,
        "pressure": 994.98,
        "sea_level": 1024.62,
        "grnd_level": 994.98,
        "humidity": 85,
        "temp_kf": -3.11
      },
      "weather": [
        {
          "id": 803,
          "main": "Clouds",
          "description": "broken clouds",
          "icon": "04d"
        }
      ],
      "clouds": {
        "all": 68
      },
      "wind": {
        "speed": 8.36,
        "deg": 299.004
      },
      "rain": {
        "3h": 0
      },
      "sys": {
        "pod": "d"
      },
      "dt_txt": "2015-03-14 21:00:00"
    },
    {
      "dt": 1426377600,
      "main": {
        "temp": 0.13,
        "temp_min": 0.13,
        "temp_max": 3.08,
        "pressure": 996.48,
        "sea_level": 1026.32,
        "grnd_level": 996.48,
        "humidity": 81,
        "temp_kf": -2.95
      },
      "weather": [
        {
          "id": 803,
          "main": "Clouds",
          "description": "broken clouds",
          "icon": "04n"
        }
      ],
      "clouds": {
        "all": 76
      },
      "wind": {
        "speed": 8.61,
        "deg": 297.502
      },
      "rain": {
        "3h": 0
      },
      "sys": {
        "pod": "n"
      },
      "dt_txt": "2015-03-15 00:00:00"
    },
    {
      "dt": 1426388400,
      "main": {
        "temp": -1.52,
        "temp_min": -1.52,
        "temp_max": 1.28,
        "pressure": 997.75,
        "sea_level": 1027.93,
        "grnd_level": 997.75,
        "humidity": 88,
        "temp_kf": -2.8
      },
      "weather": [
        {
          "id": 500,
          "main": "Rain",
          "description": "light rain",
          "icon": "10n"
        }
      ],
      "clouds": {
        "all": 80
      },
      "wind": {
        "speed": 8.46,
        "deg": 305.001
      },
      "snow": {
        "3h": 0.0225
      },
      "rain": {
        "3h": 0.025
      },
      "sys": {
        "pod": "n"
      },
      "dt_txt": "2015-03-15 03:00:00"
    },
    {
      "dt": 1426399200,
      "main": {
        "temp": -2.2,
        "temp_min": -2.2,
        "temp_max": 0.45,
        "pressure": 998.83,
        "sea_level": 1029.2,
        "grnd_level": 998.83,
        "humidity": 89,
        "temp_kf": -2.64
      },
      "weather": [
        {
          "id": 600,
          "main": "Snow",
          "description": "light snow",
          "icon": "13n"
        }
      ],
      "clouds": {
        "all": 88
      },
      "wind": {
        "speed": 9.07,
        "deg": 315
      },
      "snow": {
        "3h": 0.08
      },
      "rain": {
        "3h": 0
      },
      "sys": {
        "pod": "n"
      },
      "dt_txt": "2015-03-15 06:00:00"
    },
    {
      "dt": 1426410000,
      "main": {
        "temp": -2.7,
        "temp_min": -2.7,
        "temp_max": -0.22,
        "pressure": 1000.65,
        "sea_level": 1031.1,
        "grnd_level": 1000.65,
        "humidity": 88,
        "temp_kf": -2.49
      },
      "weather": [
        {
          "id": 600,
          "main": "Snow",
          "description": "light snow",
          "icon": "13n"
        }
      ],
      "clouds": {
        "all": 88
      },
      "wind": {
        "speed": 8.86,
        "deg": 324.509
      },
      "snow": {
        "3h": 0.0475
      },
      "rain": {
        "3h": 0
      },
      "sys": {
        "pod": "n"
      },
      "dt_txt": "2015-03-15 09:00:00"
    },
    {
      "dt": 1426420800,
      "main": {
        "temp": -3.27,
        "temp_min": -3.27,
        "temp_max": -0.94,
        "pressure": 1003.17,
        "sea_level": 1033.85,
        "grnd_level": 1003.17,
        "humidity": 87,
        "temp_kf": -2.33
      },
      "weather": [
        {
          "id": 600,
          "main": "Snow",
          "description": "light snow",
          "icon": "13d"
        }
      ],
      "clouds": {
        "all": 32
      },
      "wind": {
        "speed": 7.73,
        "deg": 330.501
      },
      "snow": {
        "3h": 0.0325
      },
      "rain": {
        "3h": 0
      },
      "sys": {
        "pod": "d"
      },
      "dt_txt": "2015-03-15 12:00:00"
    },
    {
      "dt": 1426431600,
      "main": {
        "temp": -0.39,
        "temp_min": -0.39,
        "temp_max": 1.79,
        "pressure": 1005.46,
        "sea_level": 1035.84,
        "grnd_level": 1005.46,
        "humidity": 88,
        "temp_kf": -2.18
      },
      "weather": [
        {
          "id": 800,
          "main": "Clear",
          "description": "sky is clear",
          "icon": "01d"
        }
      ],
      "clouds": {
        "all": 0
      },
      "wind": {
        "speed": 7.15,
        "deg": 334.004
      },
      "snow": {
        "3h": 0
      },
      "rain": {
        "3h": 0
      },
      "sys": {
        "pod": "d"
      },
      "dt_txt": "2015-03-15 15:00:00"
    },
    {
      "dt": 1426442400,
      "main": {
        "temp": 1.82,
        "temp_min": 1.82,
        "temp_max": 3.84,
        "pressure": 1006.11,
        "sea_level": 1036.26,
        "grnd_level": 1006.11,
        "humidity": 90,
        "temp_kf": -2.02
      },
      "weather": [
        {
          "id": 800,
          "main": "Clear",
          "description": "sky is clear",
          "icon": "01d"
        }
      ],
      "clouds": {
        "all": 0
      },
      "wind": {
        "speed": 6.67,
        "deg": 326.5
      },
      "snow": {
        "3h": 0
      },
      "rain": {
        "3h": 0
      },
      "sys": {
        "pod": "d"
      },
      "dt_txt": "2015-03-15 18:00:00"
    },
    {
      "dt": 1426453200,
      "main": {
        "temp": 2.5,
        "temp_min": 2.5,
        "temp_max": 4.37,
        "pressure": 1005.86,
        "sea_level": 1035.71,
        "grnd_level": 1005.86,
        "humidity": 88,
        "temp_kf": -1.86
      },
      "weather": [
        {
          "id": 800,
          "main": "Clear",
          "description": "sky is clear",
          "icon": "01d"
        }
      ],
      "clouds": {
        "all": 0
      },
      "wind": {
        "speed": 4.81,
        "deg": 314.002
      },
      "snow": {
        "3h": 0
      },
      "rain": {
        "3h": 0
      },
      "sys": {
        "pod": "d"
      },
      "dt_txt": "2015-03-15 21:00:00"
    },
    {
      "dt": 1426464000,
      "main": {
        "temp": -1.09,
        "temp_min": -1.09,
        "temp_max": 0.62,
        "pressure": 1005.39,
        "sea_level": 1035.53,
        "grnd_level": 1005.39,
        "humidity": 88,
        "temp_kf": -1.71
      },
      "weather": [
        {
          "id": 802,
          "main": "Clouds",
          "description": "scattered clouds",
          "icon": "03n"
        }
      ],
      "clouds": {
        "all": 36
      },
      "wind": {
        "speed": 0.88,
        "deg": 267.001
      },
      "snow": {
        "3h": 0
      },
      "rain": {
        "3h": 0
      },
      "sys": {
        "pod": "n"
      },
      "dt_txt": "2015-03-16 00:00:00"
    },
    {
      "dt": 1426474800,
      "main": {
        "temp": -1.7,
        "temp_min": -1.7,
        "temp_max": -0.14,
        "pressure": 1003.61,
        "sea_level": 1033.99,
        "grnd_level": 1003.61,
        "humidity": 80,
        "temp_kf": -1.55
      },
      "weather": [
        {
          "id": 803,
          "main": "Clouds",
          "description": "broken clouds",
          "icon": "04n"
        }
      ],
      "clouds": {
        "all": 76
      },
      "wind": {
        "speed": 3.31,
        "deg": 164
      },
      "snow": {
        "3h": 0
      },
      "rain": {
        "3h": 0
      },
      "sys": {
        "pod": "n"
      },
      "dt_txt": "2015-03-16 03:00:00"
    },
    {
      "dt": 1426485600,
      "main": {
        "temp": 0.51,
        "temp_min": 0.51,
        "temp_max": 1.91,
        "pressure": 1001.38,
        "sea_level": 1031.69,
        "grnd_level": 1001.38,
        "humidity": 87,
        "temp_kf": -1.4
      },
      "weather": [
        {
          "id": 800,
          "main": "Clear",
          "description": "sky is clear",
          "icon": "01n"
        }
      ],
      "clouds": {
        "all": 88
      },
      "wind": {
        "speed": 4.82,
        "deg": 196.502
      },
      "snow": {
        "3h": 0.0025
      },
      "rain": {
        "3h": 0
      },
      "sys": {
        "pod": "n"
      },
      "dt_txt": "2015-03-16 06:00:00"
    },
    {
      "dt": 1426496400,
      "main": {
        "temp": 3.15,
        "temp_min": 3.15,
        "temp_max": 4.4,
        "pressure": 998.13,
        "sea_level": 1028.34,
        "grnd_level": 998.13,
        "humidity": 89,
        "temp_kf": -1.24
      },
      "weather": [
        {
          "id": 803,
          "main": "Clouds",
          "description": "broken clouds",
          "icon": "04n"
        }
      ],
      "clouds": {
        "all": 76
      },
      "wind": {
        "speed": 5.98,
        "deg": 208.5
      },
      "snow": {
        "3h": 0
      },
      "rain": {
        "3h": 0
      },
      "sys": {
        "pod": "n"
      },
      "dt_txt": "2015-03-16 09:00:00"
    },
    {
      "dt": 1426507200,
      "main": {
        "temp": 4.11,
        "temp_min": 4.11,
        "temp_max": 5.19,
        "pressure": 996.64,
        "sea_level": 1026.76,
        "grnd_level": 996.64,
        "humidity": 90,
        "temp_kf": -1.09
      },
      "weather": [
        {
          "id": 802,
          "main": "Clouds",
          "description": "scattered clouds",
          "icon": "03d"
        }
      ],
      "clouds": {
        "all": 44
      },
      "wind": {
        "speed": 5.46,
        "deg": 224.502
      },
      "snow": {
        "3h": 0
      },
      "rain": {
        "3h": 0
      },
      "sys": {
        "pod": "d"
      },
      "dt_txt": "2015-03-16 12:00:00"
    },
    {
      "dt": 1426518000,
      "main": {
        "temp": 8.57,
        "temp_min": 8.57,
        "temp_max": 9.51,
        "pressure": 996.53,
        "sea_level": 1026.26,
        "grnd_level": 996.53,
        "humidity": 89,
        "temp_kf": -0.93
      },
      "weather": [
        {
          "id": 802,
          "main": "Clouds",
          "description": "scattered clouds",
          "icon": "03d"
        }
      ],
      "clouds": {
        "all": 32
      },
      "wind": {
        "speed": 5.31,
        "deg": 282.5
      },
      "snow": {
        "3h": 0
      },
      "rain": {
        "3h": 0
      },
      "sys": {
        "pod": "d"
      },
      "dt_txt": "2015-03-16 15:00:00"
    },
    {
      "dt": 1426528800,
      "main": {
        "temp": 9.21,
        "temp_min": 9.21,
        "temp_max": 9.99,
        "pressure": 997.52,
        "sea_level": 1026.97,
        "grnd_level": 997.52,
        "humidity": 93,
        "temp_kf": -0.78
      },
      "weather": [
        {
          "id": 803,
          "main": "Clouds",
          "description": "broken clouds",
          "icon": "04d"
        }
      ],
      "clouds": {
        "all": 64
      },
      "wind": {
        "speed": 7.18,
        "deg": 325.5
      },
      "snow": {
        "3h": 0
      },
      "rain": {
        "3h": 0
      },
      "sys": {
        "pod": "d"
      },
      "dt_txt": "2015-03-16 18:00:00"
    },
    {
      "dt": 1426539600,
      "main": {
        "temp": 8.08,
        "temp_min": 8.08,
        "temp_max": 8.7,
        "pressure": 998.56,
        "sea_level": 1028.01,
        "grnd_level": 998.56,
        "humidity": 85,
        "temp_kf": -0.62
      },
      "weather": [
        {
          "id": 803,
          "main": "Clouds",
          "description": "broken clouds",
          "icon": "04d"
        }
      ],
      "clouds": {
        "all": 76
      },
      "wind": {
        "speed": 6.35,
        "deg": 326.002
      },
      "snow": {
        "3h": 0
      },
      "rain": {
        "3h": 0
      },
      "sys": {
        "pod": "d"
      },
      "dt_txt": "2015-03-16 21:00:00"
    },
    {
      "dt": 1426550400,
      "main": {
        "temp": 4.98,
        "temp_min": 4.98,
        "temp_max": 5.44,
        "pressure": 999.22,
        "sea_level": 1028.95,
        "grnd_level": 999.22,
        "humidity": 90,
        "temp_kf": -0.47
      },
      "weather": [
        {
          "id": 500,
          "main": "Rain",
          "description": "light rain",
          "icon": "10n"
        }
      ],
      "clouds": {
        "all": 92
      },
      "wind": {
        "speed": 3.86,
        "deg": 323.006
      },
      "snow": {
        "3h": 0
      },
      "rain": {
        "3h": 0.32
      },
      "sys": {
        "pod": "n"
      },
      "dt_txt": "2015-03-17 00:00:00"
    },
    {
      "dt": 1426561200,
      "main": {
        "temp": 2.88,
        "temp_min": 2.88,
        "temp_max": 3.2,
        "pressure": 999.82,
        "sea_level": 1029.87,
        "grnd_level": 999.82,
        "humidity": 100,
        "temp_kf": -0.31
      },
      "weather": [
        {
          "id": 500,
          "main": "Rain",
          "description": "light rain",
          "icon": "10n"
        }
      ],
      "clouds": {
        "all": 92
      },
      "wind": {
        "speed": 3.88,
        "deg": 322.002
      },
      "snow": {
        "3h": 0
      },
      "rain": {
        "3h": 0.72
      },
      "sys": {
        "pod": "n"
      },
      "dt_txt": "2015-03-17 03:00:00"
    },
    {
      "dt": 1426572000,
      "main": {
        "temp": 0.92,
        "temp_min": 0.92,
        "temp_max": 1.07,
        "pressure": 999.3,
        "sea_level": 1029.59,
        "grnd_level": 999.3,
        "humidity": 97,
        "temp_kf": -0.16
      },
      "weather": [
        {
          "id": 500,
          "main": "Rain",
          "description": "light rain",
          "icon": "10n"
        }
      ],
      "clouds": {
        "all": 92
      },
      "wind": {
        "speed": 6.27,
        "deg": 341.002
      },
      "snow": {
        "3h": 0.22
      },
      "rain": {
        "3h": 0.53
      },
      "sys": {
        "pod": "n"
      },
      "dt_txt": "2015-03-17 06:00:00"
    },
    {
      "dt": 1426582800,
      "main": {
        "temp": -0.49,
        "temp_min": -0.49,
        "temp_max": -0.49,
        "pressure": 999.36,
        "sea_level": 1030.04,
        "grnd_level": 999.36,
        "humidity": 95
      },
      "weather": [
        {
          "id": 500,
          "main": "Rain",
          "description": "light rain",
          "icon": "10n"
        }
      ],
      "clouds": {
        "all": 92
      },
      "wind": {
        "speed": 5.12,
        "deg": 350.501
      },
      "snow": {
        "3h": 1.405
      },
      "rain": {
        "3h": 0.01
      },
      "sys": {
        "pod": "n"
      },
      "dt_txt": "2015-03-17 09:00:00"
    },
    {
      "dt": 1426593600,
      "main": {
        "temp": -1.72,
        "temp_min": -1.72,
        "temp_max": -1.72,
        "pressure": 1002.11,
        "sea_level": 1032.72,
        "grnd_level": 1002.11,
        "humidity": 86
      },
      "weather": [
        {
          "id": 600,
          "main": "Snow",
          "description": "light snow",
          "icon": "13d"
        }
      ],
      "clouds": {
        "all": 80
      },
      "wind": {
        "speed": 5.27,
        "deg": 15.0003
      },
      "snow": {
        "3h": 0.19
      },
      "rain": {
        "3h": 0
      },
      "sys": {
        "pod": "d"
      },
      "dt_txt": "2015-03-17 12:00:00"
    },
    {
      "dt": 1426604400,
      "main": {
        "temp": -1.25,
        "temp_min": -1.25,
        "temp_max": -1.25,
        "pressure": 1003.66,
        "sea_level": 1034.29,
        "grnd_level": 1003.66,
        "humidity": 89
      },
      "weather": [
        {
          "id": 803,
          "main": "Clouds",
          "description": "broken clouds",
          "icon": "04d"
        }
      ],
      "clouds": {
        "all": 68
      },
      "wind": {
        "speed": 4.77,
        "deg": 4.00809
      },
      "snow": {
        "3h": 0
      },
      "rain": {
        "3h": 0
      },
      "sys": {
        "pod": "d"
      },
      "dt_txt": "2015-03-17 15:00:00"
    },
    {
      "dt": 1426615200,
      "main": {
        "temp": 1.22,
        "temp_min": 1.22,
        "temp_max": 1.22,
        "pressure": 1003.77,
        "sea_level": 1034.31,
        "grnd_level": 1003.77,
        "humidity": 84
      },
      "weather": [
        {
          "id": 800,
          "main": "Clear",
          "description": "sky is clear",
          "icon": "01d"
        }
      ],
      "clouds": {
        "all": 0
      },
      "wind": {
        "speed": 5.68,
        "deg": 333.503
      },
      "snow": {
        "3h": 0
      },
      "rain": {
        "3h": 0
      },
      "sys": {
        "pod": "d"
      },
      "dt_txt": "2015-03-17 18:00:00"
    },
    {
      "dt": 1426626000,
      "main": {
        "temp": 1.19,
        "temp_min": 1.19,
        "temp_max": 1.19,
        "pressure": 1003.2,
        "sea_level": 1033.6,
        "grnd_level": 1003.2,
        "humidity": 75
      },
      "weather": [
        {
          "id": 800,
          "main": "Clear",
          "description": "sky is clear",
          "icon": "01d"
        }
      ],
      "clouds": {
        "all": 0
      },
      "wind": {
        "speed": 7.15,
        "deg": 324.501
      },
      "snow": {
        "3h": 0
      },
      "rain": {
        "3h": 0
      },
      "sys": {
        "pod": "d"
      },
      "dt_txt": "2015-03-17 21:00:00"
    },
    {
      "dt": 1426636800,
      "main": {
        "temp": -1.34,
        "temp_min": -1.34,
        "temp_max": -1.34,
        "pressure": 1004.62,
        "sea_level": 1035.28,
        "grnd_level": 1004.62,
        "humidity": 75
      },
      "weather": [
        {
          "id": 800,
          "main": "Clear",
          "description": "sky is clear",
          "icon": "01n"
        }
      ],
      "clouds": {
        "all": 80
      },
      "wind": {
        "speed": 6.96,
        "deg": 336.001
      },
      "snow": {
        "3h": 0.0049999999999999
      },
      "rain": {
        "3h": 0
      },
      "sys": {
        "pod": "n"
      },
      "dt_txt": "2015-03-18 00:00:00"
    },
    {
      "dt": 1426647600,
      "main": {
        "temp": -4.51,
        "temp_min": -4.51,
        "temp_max": -4.51,
        "pressure": 1007.06,
        "sea_level": 1038.22,
        "grnd_level": 1007.06,
        "humidity": 70
      },
      "weather": [
        {
          "id": 800,
          "main": "Clear",
          "description": "sky is clear",
          "icon": "01n"
        }
      ],
      "clouds": {
        "all": 0
      },
      "wind": {
        "speed": 9.76,
        "deg": 339.502
      },
      "snow": {
        "3h": 0.015
      },
      "rain": {
        "3h": 0
      },
      "sys": {
        "pod": "n"
      },
      "dt_txt": "2015-03-18 03:00:00"
    },
    {
      "dt": 1426658400,
      "main": {
        "temp": -6.63,
        "temp_min": -6.63,
        "temp_max": -6.63,
        "pressure": 1008.7,
        "sea_level": 1040.07,
        "grnd_level": 1008.7,
        "humidity": 74
      },
      "weather": [
        {
          "id": 800,
          "main": "Clear",
          "description": "sky is clear",
          "icon": "01n"
        }
      ],
      "clouds": {
        "all": 0
      },
      "wind": {
        "speed": 9.17,
        "deg": 334.502
      },
      "snow": {
        "3h": 0
      },
      "rain": {
        "3h": 0
      },
      "sys": {
        "pod": "n"
      },
      "dt_txt": "2015-03-18 06:00:00"
    },
    {
      "dt": 1426669200,
      "main": {
        "temp": -7.39,
        "temp_min": -7.39,
        "temp_max": -7.39,
        "pressure": 1009.8,
        "sea_level": 1041.32,
        "grnd_level": 1009.8,
        "humidity": 69
      },
      "weather": [
        {
          "id": 800,
          "main": "Clear",
          "description": "sky is clear",
          "icon": "01n"
        }
      ],
      "clouds": {
        "all": 0
      },
      "wind": {
        "speed": 7.07,
        "deg": 344.501
      },
      "snow": {
        "3h": 0
      },
      "rain": {
        "3h": 0
      },
      "sys": {
        "pod": "n"
      },
      "dt_txt": "2015-03-18 09:00:00"
    },
    {
      "dt": 1426680000,
      "main": {
        "temp": -8.51,
        "temp_min": -8.51,
        "temp_max": -8.51,
        "pressure": 1010.88,
        "sea_level": 1042.46,
        "grnd_level": 1010.88,
        "humidity": 65
      },
      "weather": [
        {
          "id": 800,
          "main": "Clear",
          "description": "sky is clear",
          "icon": "01d"
        }
      ],
      "clouds": {
        "all": 0
      },
      "wind": {
        "speed": 2.95,
        "deg": 340.501
      },
      "snow": {
        "3h": 0
      },
      "rain": {
        "3h": 0
      },
      "sys": {
        "pod": "d"
      },
      "dt_txt": "2015-03-18 12:00:00"
    },
    {
      "dt": 1426690800,
      "main": {
        "temp": -2.83,
        "temp_min": -2.83,
        "temp_max": -2.83,
        "pressure": 1010.98,
        "sea_level": 1042.44,
        "grnd_level": 1010.98,
        "humidity": 85
      },
      "weather": [
        {
          "id": 800,
          "main": "Clear",
          "description": "sky is clear",
          "icon": "01d"
        }
      ],
      "clouds": {
        "all": 0
      },
      "wind": {
        "speed": 3.8,
        "deg": 284.001
      },
      "snow": {
        "3h": 0
      },
      "rain": {
        "3h": 0
      },
      "sys": {
        "pod": "d"
      },
      "dt_txt": "2015-03-18 15:00:00"
    },
    {
      "dt": 1426701600,
      "main": {
        "temp": -0.29,
        "temp_min": -0.29,
        "temp_max": -0.29,
        "pressure": 1009.86,
        "sea_level": 1040.72,
        "grnd_level": 1009.86,
        "humidity": 78
      },
      "weather": [
        {
          "id": 800,
          "main": "Clear",
          "description": "sky is clear",
          "icon": "01d"
        }
      ],
      "clouds": {
        "all": 0
      },
      "wind": {
        "speed": 7,
        "deg": 291.5
      },
      "snow": {
        "3h": 0
      },
      "rain": {
        "3h": 0
      },
      "sys": {
        "pod": "d"
      },
      "dt_txt": "2015-03-18 18:00:00"
    },
    {
      "dt": 1426712400,
      "main": {
        "temp": 1.03,
        "temp_min": 1.03,
        "temp_max": 1.03,
        "pressure": 1007.88,
        "sea_level": 1038.53,
        "grnd_level": 1007.88,
        "humidity": 69
      },
      "weather": [
        {
          "id": 800,
          "main": "Clear",
          "description": "sky is clear",
          "icon": "01d"
        }
      ],
      "clouds": {
        "all": 0
      },
      "wind": {
        "speed": 6.02,
        "deg": 293.501
      },
      "snow": {
        "3h": 0
      },
      "rain": {
        "3h": 0
      },
      "sys": {
        "pod": "d"
      },
      "dt_txt": "2015-03-18 21:00:00"
    },
    {
      "dt": 1426723200,
      "main": {
        "temp": -1.07,
        "temp_min": -1.07,
        "temp_max": -1.07,
        "pressure": 1007.1,
        "sea_level": 1037.91,
        "grnd_level": 1007.1,
        "humidity": 63
      },
      "weather": [
        {
          "id": 800,
          "main": "Clear",
          "description": "sky is clear",
          "icon": "01n"
        }
      ],
      "clouds": {
        "all": 0
      },
      "wind": {
        "speed": 3.41,
        "deg": 262
      },
      "snow": {
        "3h": 0
      },
      "rain": {
        "3h": 0
      },
      "sys": {
        "pod": "n"
      },
      "dt_txt": "2015-03-19 00:00:00"
    },
    {
      "dt": 1426734000,
      "main": {
        "temp": -3.15,
        "temp_min": -3.15,
        "temp_max": -3.15,
        "pressure": 1007,
        "sea_level": 1038.03,
        "grnd_level": 1007,
        "humidity": 70
      },
      "weather": [
        {
          "id": 800,
          "main": "Clear",
          "description": "sky is clear",
          "icon": "01n"
        }
      ],
      "clouds": {
        "all": 0
      },
      "wind": {
        "speed": 4.52,
        "deg": 241.501
      },
      "snow": {
        "3h": 0
      },
      "rain": {
        "3h": 0
      },
      "sys": {
        "pod": "n"
      },
      "dt_txt": "2015-03-19 03:00:00"
    },
    {
      "dt": 1426744800,
      "main": {
        "temp": -3.57,
        "temp_min": -3.57,
        "temp_max": -3.57,
        "pressure": 1006.48,
        "sea_level": 1037.55,
        "grnd_level": 1006.48,
        "humidity": 76
      },
      "weather": [
        {
          "id": 802,
          "main": "Clouds",
          "description": "scattered clouds",
          "icon": "03n"
        }
      ],
      "clouds": {
        "all": 36
      },
      "wind": {
        "speed": 4.21,
        "deg": 250.005
      },
      "snow": {
        "3h": 0
      },
      "rain": {
        "3h": 0
      },
      "sys": {
        "pod": "n"
      },
      "dt_txt": "2015-03-19 06:00:00"
    },
    {
      "dt": 1426755600,
      "main": {
        "temp": -4.29,
        "temp_min": -4.29,
        "temp_max": -4.29,
        "pressure": 1005.92,
        "sea_level": 1037.02,
        "grnd_level": 1005.92,
        "humidity": 76
      },
      "weather": [
        {
          "id": 803,
          "main": "Clouds",
          "description": "broken clouds",
          "icon": "04n"
        }
      ],
      "clouds": {
        "all": 64
      },
      "wind": {
        "speed": 3.66,
        "deg": 228.502
      },
      "snow": {
        "3h": 0
      },
      "rain": {
        "3h": 0
      },
      "sys": {
        "pod": "n"
      },
      "dt_txt": "2015-03-19 09:00:00"
    },
    {
      "dt": 1426766400,
      "main": {
        "temp": -3.84,
        "temp_min": -3.84,
        "temp_max": -3.84,
        "pressure": 1006,
        "sea_level": 1037.03,
        "grnd_level": 1006,
        "humidity": 77
      },
      "weather": [
        {
          "id": 803,
          "main": "Clouds",
          "description": "broken clouds",
          "icon": "04d"
        }
      ],
      "clouds": {
        "all": 80
      },
      "wind": {
        "speed": 3.16,
        "deg": 226
      },
      "snow": {
        "3h": 0
      },
      "rain": {
        "3h": 0
      },
      "sys": {
        "pod": "d"
      },
      "dt_txt": "2015-03-19 12:00:00"
    },
    {
      "dt": 1426777200,
      "main": {
        "temp": 0.65,
        "temp_min": 0.65,
        "temp_max": 0.65,
        "pressure": 1005.71,
        "sea_level": 1036.41,
        "grnd_level": 1005.71,
        "humidity": 78
      },
      "weather": [
        {
          "id": 803,
          "main": "Clouds",
          "description": "broken clouds",
          "icon": "04d"
        }
      ],
      "clouds": {
        "all": 80
      },
      "wind": {
        "speed": 3.42,
        "deg": 223.5
      },
      "snow": {
        "3h": 0
      },
      "rain": {
        "3h": 0
      },
      "sys": {
        "pod": "d"
      },
      "dt_txt": "2015-03-19 15:00:00"
    },
    {
      "dt": 1426788000,
      "main": {
        "temp": 3.99,
        "temp_min": 3.99,
        "temp_max": 3.99,
        "pressure": 1004.19,
        "sea_level": 1034.6,
        "grnd_level": 1004.19,
        "humidity": 73
      },
      "weather": [
        {
          "id": 803,
          "main": "Clouds",
          "description": "broken clouds",
          "icon": "04d"
        }
      ],
      "clouds": {
        "all": 68
      },
      "wind": {
        "speed": 4.57,
        "deg": 234
      },
      "snow": {
        "3h": 0
      },
      "rain": {
        "3h": 0
      },
      "sys": {
        "pod": "d"
      },
      "dt_txt": "2015-03-19 18:00:00"
    },
    {
      "dt": 1426798800,
      "main": {
        "temp": 4.78,
        "temp_min": 4.78,
        "temp_max": 4.78,
        "pressure": 1002.07,
        "sea_level": 1032.39,
        "grnd_level": 1002.07,
        "humidity": 69
      },
      "weather": [
        {
          "id": 803,
          "main": "Clouds",
          "description": "broken clouds",
          "icon": "04d"
        }
      ],
      "clouds": {
        "all": 76
      },
      "wind": {
        "speed": 4.46,
        "deg": 235.004
      },
      "snow": {
        "3h": 0
      },
      "rain": {
        "3h": 0
      },
      "sys": {
        "pod": "d"
      },
      "dt_txt": "2015-03-19 21:00:00"
    }
  ]
};

var dailyforecast_response = {
  "cod": "200",
  "message": 0.932,
  "city": {
    "id": 6058560,
    "name": "London",
    "coord": {
      "lon": -81.23304,
      "lat": 42.983391
    },
    "country": "CA",
    "population": 346765
  },
  "cnt": 7,
  "list": [
    {
      "dt": 1426352400,
      "temp": {
        "day": 2.56,
        "min": -2.16,
        "max": 2.56,
        "night": -2.16,
        "eve": -0.14,
        "morn": 2.56
      },
      "pressure": 986.22,
      "humidity": 84,
      "weather": [
        {
          "id": 600,
          "main": "Snow",
          "description": "light snow",
          "icon": "13d"
        }
      ],
      "speed": 8.61,
      "deg": 295,
      "clouds": 36,
      "snow": 0.24
    },
    {
      "dt": 1426438800,
      "temp": {
        "day": 2.4,
        "min": -3.96,
        "max": 3.89,
        "night": -1.79,
        "eve": -0.47,
        "morn": -3.57
      },
      "pressure": 996.87,
      "humidity": 83,
      "weather": [
        {
          "id": 600,
          "main": "Snow",
          "description": "light snow",
          "icon": "13d"
        }
      ],
      "speed": 6.92,
      "deg": 322,
      "clouds": 8,
      "snow": 0.03
    },
    {
      "dt": 1426525200,
      "temp": {
        "day": 10.48,
        "min": 0.45,
        "max": 10.48,
        "night": 0.45,
        "eve": 5.34,
        "morn": 3.33
      },
      "pressure": 988.52,
      "humidity": 87,
      "weather": [
        {
          "id": 600,
          "main": "Snow",
          "description": "light snow",
          "icon": "13d"
        }
      ],
      "speed": 7.73,
      "deg": 313,
      "clouds": 64,
      "rain": 0.54,
      "snow": 0.11
    },
    {
      "dt": 1426611600,
      "temp": {
        "day": 1.97,
        "min": -7.53,
        "max": 1.97,
        "night": -7.53,
        "eve": -1.84,
        "morn": -2.17
      },
      "pressure": 994.37,
      "humidity": 70,
      "weather": [
        {
          "id": 600,
          "main": "Snow",
          "description": "light snow",
          "icon": "13d"
        }
      ],
      "speed": 5.03,
      "deg": 335,
      "clouds": 0,
      "snow": 0.39
    },
    {
      "dt": 1426698000,
      "temp": {
        "day": 1.51,
        "min": -8.28,
        "max": 1.51,
        "night": -1.86,
        "eve": 0.2,
        "morn": -8.28
      },
      "pressure": 1006.11,
      "humidity": 0,
      "weather": [
        {
          "id": 600,
          "main": "Snow",
          "description": "light snow",
          "icon": "13d"
        }
      ],
      "speed": 4.61,
      "deg": 278,
      "clouds": 0,
      "snow": 0.05
    },
    {
      "dt": 1426784400,
      "temp": {
        "day": 2.9,
        "min": -3.75,
        "max": 2.9,
        "night": -2.22,
        "eve": -2.22,
        "morn": -3.75
      },
      "pressure": 1002.1,
      "humidity": 0,
      "weather": [
        {
          "id": 800,
          "main": "Clear",
          "description": "sky is clear",
          "icon": "01d"
        }
      ],
      "speed": 5.2,
      "deg": 326,
      "clouds": 0
    },
    {
      "dt": 1426870800,
      "temp": {
        "day": -2.22,
        "min": -2.22,
        "max": -1.28,
        "night": -1.28,
        "eve": -1.28,
        "morn": -2.22
      },
      "pressure": 1003.79,
      "humidity": 0,
      "weather": [
        {
          "id": 600,
          "main": "Snow",
          "description": "light snow",
          "icon": "13d"
        }
      ],
      "speed": 5.03,
      "deg": 348,
      "clouds": 0,
      "snow": 0.51
    }
  ]
};