from pinotdb import connect
import pandas as pd
import streamlit as st
import numpy as np
import altair as alt
import plotly.express as px

st.title('SpaceX Launch Statistics')
st.markdown("Perform exploratory data analysis on SpaceX launch data set with Apache Pinot")

conn = connect(host='localhost', port=8000, path='/query/sql', scheme='http')
curs = conn.cursor()

# Breakdown of the landing outcome
st.subheader('Breakdown of landing outcome')
st.markdown("What are difference landing outcomes with their frequencies? What's the % of successul launches?")
curs.execute("""
    SELECT
     landing_outcome,count(landing_outcome) as frequency
    FROM launches
    GROUP BY landing_outcome
    LIMIT 200
""")
df = pd.DataFrame(curs, columns=[item[0] for item in curs.description])
fig = px.pie(df, values='frequency', names='landing_outcome')
st.plotly_chart(fig, use_container_width=True)

# Launches by customer
st.subheader('Launches by customer')
st.markdown("Which customer has spent most money on SpaceX? Apparently, it is NASA.")
curs.execute("""
    SELECT 
        customer,
        count(customer) as total_launches
    FROM launches 
    GROUP BY customer
    ORDER BY total_launches DESC
""")
df = pd.DataFrame(curs, columns=[item[0] for item in curs.description])
chart = alt.Chart(df).mark_bar().encode(
    x='customer:N',
    y='total_launches:Q'
)
st.altair_chart(chart,use_container_width=True)

# Let's calaculate launches per launch site
st.subheader('Launches per launch site')
st.markdown("What were the launch sites used for the missions? How did each site attribute to the total? Which site had contributed to the most successful landings?  ")
curs.execute("""
    SELECT 
        launch_site,
        landing_outcome,
        count(launch_site) as frequency
    FROM launches 
    GROUP BY launch_site,landing_outcome
    LIMIT 200
""")
df = pd.DataFrame(curs, columns=[item[0] for item in curs.description])
chart = alt.Chart(df).mark_bar().encode(
    x='launch_site:N',
    y='frequency:Q',
    color='landing_outcome:N'
)
st.altair_chart(chart,use_container_width=True)

# Payload mass variation by year
st.subheader('Payload mass variation over the years')
st.markdown("How did the payload capacity of each mission varied over the past then years?")
curs.execute("""
    SELECT 
        year(FromDateTime(launch_date,'dd-MM-yyyy')) as launch_year,
        payload_mass_kg
    FROM launches
""")
df = pd.DataFrame(curs, columns=[item[0] for item in curs.description])
chart = alt.Chart(df).mark_line().encode(
    x='launch_year:N',
    y='sum(payload_mass_kg):Q'
)
st.altair_chart(chart,use_container_width=True)

# Close the connection and clean up the resources
conn = None