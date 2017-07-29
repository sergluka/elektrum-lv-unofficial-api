{
  "dashboard": {
    "annotations": {
      "list": []
    },
    "editable": true,
    "gnetId": null,
    "graphTooltip": 0,
    "hideControls": false,
    "id": 1,
    "links": [],
    "refresh": false,
    "rows": [
      {
        "collapse": false,
        "height": "250px",
        "panels": [
          {
            "alert": {
              "conditions": [
                {
                  "evaluator": {
                    "params": [
                      0.5
                    ],
                    "type": "gt"
                  },
                  "operator": {
                    "type": "and"
                  },
                  "query": {
                    "params": [
                      "B",
                      "5m",
                      "now"
                    ]
                  },
                  "reducer": {
                    "params": [],
                    "type": "max"
                  },
                  "type": "query"
                }
              ],
              "executionErrorState": "keep_state",
              "frequency": "60s",
              "handler": 1,
              "name": "Consumption per day alert",
              "noDataState": "keep_state",
              "notifications": []
            },
            "aliasColors": {},
            "bars": false,
            "dashLength": 10,
            "dashes": false,
            "datasource": "InfluxDB",
            "decimals": 4,
            "fill": 1,
            "id": 1,
            "legend": {
              "alignAsTable": true,
              "avg": false,
              "current": false,
              "max": false,
              "min": false,
              "rightSide": true,
              "show": false,
              "total": true,
              "values": true
            },
            "lines": true,
            "linewidth": 1,
            "links": [],
            "nullPointMode": "null",
            "percentage": false,
            "pointradius": 5,
            "points": false,
            "renderer": "flot",
            "seriesOverrides": [],
            "spaceLength": 10,
            "span": 12,
            "stack": false,
            "steppedLine": false,
            "targets": [
              {
                "alias": "spend",
                "dsType": "influxdb",
                "groupBy": [],
                "measurement": "day",
                "orderByTime": "ASC",
                "policy": "default",
                "refId": "B",
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
                "colorMode": "critical",
                "fill": true,
                "line": true,
                "op": "gt",
                "value": 0.5
              }
            ],
            "timeFrom": null,
            "timeShift": null,
            "title": "Consumption per day",
            "tooltip": {
              "shared": true,
              "sort": 0,
              "value_type": "individual"
            },
            "type": "graph",
            "xaxis": {
              "buckets": null,
              "mode": "time",
              "name": null,
              "show": true,
              "values": []
            },
            "yaxes": [
              {
                "format": "short",
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
                "show": true
              }
            ]
          }
        ],
        "repeat": null,
        "repeatIteration": null,
        "repeatRowId": null,
        "showTitle": false,
        "title": "Dashboard Row",
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
      "from": "now/w",
      "to": "now/w"
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
    "id": null,
    "timezone": "browser",
    "title": "Electricity",
    "version": 1
  }
}
