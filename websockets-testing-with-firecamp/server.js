const WebSocket = require('ws');

const PORT = 8080;
const wss = new WebSocket.Server({ port: PORT });

wss.on('connection', (ws) => {
  const clientId = uuidv4();
  console.log("Connection accepted from client: " + clientId)
  ws.send('Welcome to the toUpper() service!');

  ws.on('message', function message(data) {
    const message = data.toString();
    console.log('received: %s', message);
    ws.send(message.toUpperCase());
  });

  ws.on("close", () => {
    console.log("Websocket client " + ws + " has been disconnected.")
  });
});

//A function to generate a unique client ID
function uuidv4() {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
    var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
    return v.toString(16);
  });
}

console.log("Websocket server has been started and listening on port " + PORT);