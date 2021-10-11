import psycopg2
import pandas.io.sql as sqlio
import pandas as pd
import dash
from dash import dcc
from dash import html
import plotly.express as px

app = dash.Dash(__name__)

# Connect to Materialize as a regular database
conn = psycopg2.connect("dbname=materialize user=materialize port=6875 host=localhost")

# Read the materialized view with Pandas
sql = "select * from consolidated_funnel order by cnt desc;"
df = pd.read_sql_query(sql, conn)

# Plot a funnel chart
fig = px.funnel(df, x="step", y="cnt")

# Main UI scaffolding for the dashboard
app.layout = html.Div(children=[
    html.H1(children='Conversion Funnel'),

    html.Div(children='''
        Dash: A web application framework for your data.
    '''),

    dcc.Graph(
        id='funnel-chart',
        figure=fig
    )
])

if __name__ == '__main__':
    app.run_server(debug=True)

conn = None

