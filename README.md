# Library and application to get electric meters measures from 'elektrum.lv' site

Disclaimer: This library and application, have no any connection neither with 'elektrum.lv' site creators nor 'AS Latvenergo'.
Also, the data you get are mirrored from a site, and just how they are. Mainly, it's just a web site parser 
and realization of my idea.  

## Precondition:
- Registration for 'elektrum.lv'. If you are not a Latvian resident, most likely this application won't be useful for you. 

## Export options:

### InfluxDB

Main idea of this project is to monitor electric meters data,
add analytics, reports and visualize them. For this purposes very suits
combination of InfluxDB and Grafana. If you wish, is possible to add alarms into
Grafana for monitoring your consumption limits. But take into account 
that source data on a site has been refreshed with delay in one, two days.

![Grafana screenshot](/doc/Capture.png?raw=true)

You can use [docker-compose.yml](\opt\docker\docker-compose.yml) to run InfluxDB and Grafana in containers.

### CSV

Export data into CSV file in format `yyyy-mm-dd;kWh`

## Library usage:

    val period = Period.DAY 
    val from = LocalDate.of(2016, 1, 1)
    val to = LocalDate.now()
    ElektrumFetcher().use {
        it.authorize("user@inbox.lv", "password")
        val meters = when (period) {
            Period.YEAR -> it.fetchMonthly(from, to)
            Period.MONTH -> it.fetchDaily(from, to)
            Period.DAY -> it.fetchHourly(from, to)
        }
        println(meters)
    }

## Application usage:
- Download latest 'elektrum-fetcher-xx.jar'
- Place ['settings.yml'](/opt/settings.yml) near the jar file.
- Set your credential for elektrum.lv site.
- If you plan to use export into InfluxDB, setup respective section settings. For export into CSV, it's not necessary.

### Command line examples 
 - `java -jar elektrum-fetcher-xx.jar csv --period month -o output.csv --debug 2017-01`
 
    Exports data summed by month into CSV file from 1 January 2017 till now
 - `java -jar elektrum-fetcher-xx.jar csv --period day -o output.csv 2016 2017`
 
    Exports data summed by day into CSV file from 1 January 2016 till 1 January 2017
 - `java -jar elektrum-fetcher-xx.jar influxdb --period year 2016 2017`
 
    Exports data into InfluxDB from 1 January 2016 till 1 January 2017
 - `java -jar elektrum-fetcher-xx.jar influxdb --period day --recent`
 
    Exports only recent data into InfluxDB. Note that `--recent` option works with `influxdb` only
 
