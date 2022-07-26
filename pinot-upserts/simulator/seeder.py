import random, time
from mysql.connector import connect, Error
from faker import Faker
from faker.providers import company

# CONFIG
usersLimit              = 1000
productsLimit           = 100
ordersLimit             = 100000
orderInterval           = 100
itemPriceMin            = 5
itemPriceMax            = 500
categories              = ['electronics', 'groceries', 'health', 'household', 'automotive']

mysqlHost          = 'mysql'
mysqlPort          = '3306'
mysqlUser          = 'mysqluser'
mysqlPass          = 'mysqlpw'
debeziumHostPort   = 'debezium:8083'
kafkaHostPort      = 'kafka:9092'

# INSERT TEMPLATES
insert_user_tpl        = "INSERT INTO fakeshop.users (first_name, last_name, email, country) VALUES ( %s, %s, %s, %s )"
insert_product_tpl     = "INSERT INTO fakeshop.products (name, category, price) VALUES ( %s, %s, %s )"
insert_order_tpl       = "INSERT INTO fakeshop.orders (user_id, product_id, quantity, total) VALUES ( %s, %s, %s, %s )"

fake = Faker()
fake.add_provider(company)

try:
    with connect(
        host=mysqlHost,
        user=mysqlUser,
        password=mysqlPass,
    ) as connection:
        with connection.cursor() as cursor:
            print("Seeding fakshop database...")
            cursor.executemany(
                insert_user_tpl,
                [
                    (
                        fake.first_name(),
                        fake.last_name(),
                        fake.email(),
                        fake.country()
                     ) for i in range(usersLimit)
                ]
            )
            cursor.executemany(
                insert_product_tpl,
                [
                    (
                        fake.company(),
                        random.choice(categories),
                        random.randint(itemPriceMin*100,itemPriceMax*100)/100
                    ) for i in range(productsLimit)
                ]
            )
            connection.commit()

            print("Getting product ID and PRICE as tuples...")
            cursor.execute("SELECT id, price FROM fakeshop.products")
            product_prices = [(row[0], row[1]) for row in cursor]

            print("Preparing to loop + seed orders")
            for i in range(ordersLimit):
                # Get a random a user and a product to order
                product = random.choice(product_prices)
                user = random.randint(0,usersLimit-1)
                purchase_quantity = random.randint(1,5)

                cursor.execute(
                    insert_order_tpl,
                    (
                        user,
                        product[0],
                        purchase_quantity,
                        product[1] * purchase_quantity
                    )
                )
                connection.commit()

                #Pause
                time.sleep(orderInterval/1000)

    connection.close()

except Error as e:
    print(e)