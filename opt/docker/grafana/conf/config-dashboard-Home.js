{
    "dashboard": {
      "editable": true,
      "gnetId": null,
      "graphTooltip": 1,
      "hideControls": false,
      "id": null,
      "links": [],
      "refresh": false,
      "rows": [
        {
          "collapse": false,
          "height": "250px",
          "panels": [
            {
              "aliasColors": {},
              "bars": false,
              "dashLength": 10,
              "dashes": false,
              "datasource": "InfluxDB",
              "decimals": 4,
              "fill": 1,
              "height": "250px",
              "id": 1,
              "legend": {
                "alignAsTable": false,
                "avg": false,
                "current": false,
                "max": false,
                "min": false,
                "rightSide": false,
                "show": true,
                "total": true,
                "values": true
              },
              "lines": true,
              "linewidth": 1,
              "links": [],
              "nullPointMode": "null",
              "percentage": false,
              "pointradius": 1,
              "points": false,
              "renderer": "flot",
              "seriesOverrides": [
                {
                  "alias": "daily",
                  "legend": false,
                  "steppedLine": true
                }
              ],
              "spaceLength": 10,
              "span": 12,
              "stack": false,
              "steppedLine": false,
              "targets": [
                {
                  "alias": "hourly",
                  "dsType": "influxdb",
                  "groupBy": [],
                  "measurement": "day",
                  "orderByTime": "ASC",
                  "policy": "default",
                  "query": "SELECT \"value\" FROM \"day\" WHERE $timeFilter",
                  "rawQuery": false,
                  "refId": "A",
                  "resultFormat": "time_series",
                  "select": [
                    [
                      {
                        "params": [
                          "value"
                        ],
                        "type": "field"
                      }
                    ]
                  ],
                  "tags": []
                }
              ],
              "thresholds": [
                {
                  "colorMode": "warning",
                  "fill": true,
                  "line": true,
                  "op": "gt",
                  "value": 2
                }
              ],
              "timeFrom": null,
              "timeShift": null,
              "title": "Consumption",
              "tooltip": {
                "shared": false,
                "sort": 0,
                "value_type": "individual"
              },
              "type": "graph",
              "xaxis": {
                "buckets": null,
                "mode": "time",
                "name": null,
                "show": true,
                "values": [
                  "total"
                ]
              },
              "yaxes": [
                {
                  "format": "kwatth",
                  "label": null,
                  "logBase": 1,
                  "max": "5",
                  "min": null,
                  "show": true
                },
                {
                  "format": "short",
                  "label": null,
                  "logBase": 1,
                  "max": null,
                  "min": null,
                  "show": false
                }
              ]
            },
            {
              "aliasColors": {},
              "bars": false,
              "dashLength": 10,
              "dashes": false,
              "datasource": "InfluxDB",
              "decimals": 4,
              "fill": 1,
              "height": "250px",
              "id": 2,
              "legend": {
                "alignAsTable": false,
                "avg": false,
                "current": false,
                "max": false,
                "min": false,
                "rightSide": false,
                "show": true,
                "total": true,
                "values": true
              },
              "lines": true,
              "linewidth": 1,
              "links": [],
              "nullPointMode": "null",
              "percentage": false,
              "pointradius": 1,
              "points": false,
              "renderer": "flot",
              "seriesOverrides": [
                {
                  "alias": "daily",
                  "legend": false,
                  "steppedLine": true
                }
              ],
              "spaceLength": 10,
              "span": 6,
              "stack": false,
              "steppedLine": false,
              "targets": [
                {
                  "alias": "hourly",
                  "dsType": "influxdb",
                  "groupBy": [],
                  "measurement": "day",
                  "orderByTime": "ASC",
                  "policy": "default",
                  "query": "SELECT \"value\" FROM \"day\" WHERE $timeFilter",
                  "rawQuery": false,
                  "refId": "A",
                  "resultFormat": "time_series",
                  "select": [
                    [
                      {
                        "params": [
                          "value"
                        ],
                        "type": "field"
                      }
                    ]
                  ],
                  "tags": []
                }
              ],
              "thresholds": [
                {
                  "colorMode": "warning",
                  "fill": true,
                  "line": true,
                  "op": "gt",
                  "value": 2
                }
              ],
              "timeFrom": null,
              "timeShift": "1M",
              "title": "Consumption (a month ago)",
              "tooltip": {
                "shared": false,
                "sort": 0,
                "value_type": "individual"
              },
              "type": "graph",
              "xaxis": {
                "buckets": null,
                "mode": "time",
                "name": null,
                "show": true,
                "values": [
                  "total"
                ]
              },
              "yaxes": [
                {
                  "format": "kwatth",
                  "label": null,
                  "logBase": 1,
                  "max": "5",
                  "min": null,
                  "show": true
                },
                {
                  "format": "short",
                  "label": null,
                  "logBase": 1,
                  "max": null,
                  "min": null,
                  "show": false
                }
              ]
            },
            {
              "aliasColors": {},
              "bars": false,
              "dashLength": 10,
              "dashes": false,
              "datasource": "InfluxDB",
              "decimals": 4,
              "fill": 1,
              "height": "250px",
              "id": 3,
              "legend": {
                "alignAsTable": false,
                "avg": false,
                "current": false,
                "max": false,
                "min": false,
                "rightSide": false,
                "show": true,
                "total": true,
                "values": true
              },
              "lines": true,
              "linewidth": 1,
              "links": [],
              "nullPointMode": "null",
              "percentage": false,
              "pointradius": 1,
              "points": false,
              "renderer": "flot",
              "seriesOverrides": [
                {
                  "alias": "daily",
                  "legend": false,
                  "steppedLine": true
                }
              ],
              "spaceLength": 10,
              "span": 6,
              "stack": false,
              "steppedLine": false,
              "targets": [
                {
                  "alias": "hourly",
                  "dsType": "influxdb",
                  "groupBy": [],
                  "measurement": "day",
                  "orderByTime": "ASC",
                  "policy": "default",
                  "query": "SELECT \"value\" FROM \"day\" WHERE $timeFilter",
                  "rawQuery": false,
                  "refId": "A",
                  "resultFormat": "time_series",
                  "select": [
                    [
                      {
                        "params": [
                          "value"
                        ],
                        "type": "field"
                      }
                    ]
                  ],
                  "tags": []
                }
              ],
              "thresholds": [
                {
                  "colorMode": "warning",
                  "fill": true,
                  "line": true,
                  "op": "gt",
                  "value": 2
                }
              ],
              "timeFrom": null,
              "timeShift": "6M",
              "title": "Consumption (6 months ago)",
              "tooltip": {
                "shared": false,
                "sort": 0,
                "value_type": "individual"
              },
              "type": "graph",
              "xaxis": {
                "buckets": null,
                "mode": "time",
                "name": null,
                "show": true,
                "values": [
                  "total"
                ]
              },
              "yaxes": [
                {
                  "format": "kwatth",
                  "label": null,
                  "logBase": 1,
                  "max": "5",
                  "min": null,
                  "show": true
                },
                {
                  "format": "short",
                  "label": null,
                  "logBase": 1,
                  "max": null,
                  "min": null,
                  "show": false
                }
              ]
            }
          ],
          "repeat": null,
          "repeatIteration": null,
          "repeatRowId": null,
          "showTitle": false,
          "title": "Row title",
          "titleSize": "h6"
        },
        {
          "collapse": false,
          "height": 250,
          "panels": [
            {
              "aliasColors": {},
              "bars": true,
              "dashLength": 10,
              "dashes": false,
              "datasource": "InfluxDB",
              "decimals": 4,
              "fill": 1,
              "height": "250px",
              "id": 4,
              "legend": {
                "alignAsTable": false,
                "avg": false,
                "current": false,
                "max": false,
                "min": false,
                "rightSide": false,
                "show": true,
                "total": true,
                "values": true
              },
              "lines": false,
              "linewidth": 1,
              "links": [],
              "nullPointMode": "null",
              "percentage": false,
              "pointradius": 1,
              "points": false,
              "renderer": "flot",
              "seriesOverrides": [],
              "spaceLength": 10,
              "span": 6,
              "stack": false,
              "steppedLine": false,
              "targets": [
                {
                  "alias": "mountly",
                  "dsType": "influxdb",
                  "groupBy": [],
                  "measurement": "month",
                  "orderByTime": "ASC",
                  "policy": "default",
                  "query": "SELECT \"value\" FROM \"day\" WHERE $timeFilter",
                  "rawQuery": false,
                  "refId": "A",
                  "resultFormat": "time_series",
                  "select": [
                    [
                      {
                        "params": [
                          "value"
                        ],
                        "type": "field"
                      }
                    ]
                  ],
                  "tags": []
                }
              ],
              "thresholds": [],
              "timeFrom": "1y",
              "timeShift": null,
              "title": "Consumption per day during an year",
              "tooltip": {
                "shared": false,
                "sort": 0,
                "value_type": "individual"
              },
              "type": "graph",
              "xaxis": {
                "buckets": null,
                "mode": "time",
                "name": null,
                "show": true,
                "values": [
                  "total"
                ]
              },
              "yaxes": [
                {
                  "format": "kwatth",
                  "label": null,
                  "logBase": 1,
                  "max": null,
                  "min": null,
                  "show": true
                },
                {
                  "format": "short",
                  "label": null,
                  "logBase": 1,
                  "max": null,
                  "min": null,
                  "show": false
                }
              ]
            },
            {
              "aliasColors": {},
              "bars": true,
              "dashLength": 10,
              "dashes": false,
              "datasource": "InfluxDB",
              "decimals": 4,
              "fill": 1,
              "height": "250px",
              "id": 5,
              "legend": {
                "alignAsTable": false,
                "avg": false,
                "current": false,
                "max": false,
                "min": false,
                "rightSide": false,
                "show": true,
                "total": true,
                "values": true
              },
              "lines": false,
              "linewidth": 1,
              "links": [],
              "nullPointMode": "null",
              "percentage": false,
              "pointradius": 1,
              "points": false,
              "renderer": "flot",
              "seriesOverrides": [],
              "spaceLength": 10,
              "span": 6,
              "stack": false,
              "steppedLine": false,
              "targets": [
                {
                  "alias": "yearly",
                  "dsType": "influxdb",
                  "groupBy": [],
                  "measurement": "year",
                  "orderByTime": "ASC",
                  "policy": "default",
                  "query": "SELECT \"value\" FROM \"day\" WHERE $timeFilter",
                  "rawQuery": false,
                  "refId": "A",
                  "resultFormat": "time_series",
                  "select": [
                    [
                      {
                        "params": [
                          "value"
                        ],
                        "type": "field"
                      }
                    ]
                  ],
                  "tags": []
                }
              ],
              "thresholds": [],
              "timeFrom": "1y",
              "timeShift": null,
              "title": "Consumption per month during an year",
              "tooltip": {
                "shared": false,
                "sort": 0,
                "value_type": "individual"
              },
              "type": "graph",
              "xaxis": {
                "buckets": null,
                "mode": "time",
                "name": null,
                "show": true,
                "values": [
                  "total"
                ]
              },
              "yaxes": [
                {
                  "format": "kwatth",
                  "label": null,
                  "logBase": 1,
                  "max": null,
                  "min": null,
                  "show": true
                },
                {
                  "format": "short",
                  "label": null,
                  "logBase": 1,
                  "max": null,
                  "min": null,
                  "show": false
                }
              ]
            }
          ],
          "repeat": null,
          "repeatIteration": null,
          "repeatRowId": null,
          "showTitle": true,
          "title": "per year",
          "titleSize": "h6"
        }
      ],
      "schemaVersion": 14,
      "style": "dark",
      "tags": [],
      "templating": {
        "list": []
      },
      "time": {
        "from": "now/M",
        "to": "now"
      },
      "timepicker": {
        "refresh_intervals": [
          "5s",
          "10s",
          "30s",
          "1m",
          "5m",
          "15m",
          "30m",
          "1h",
          "2h",
          "1d"
        ],
        "time_options": [
          "5m",
          "15m",
          "1h",
          "6h",
          "12h",
          "24h",
          "2d",
          "7d",
          "30d"
        ]
      },
      "timezone": "utc",
      "title": "Electricity",
      "version": 1
    }
}
