# hombot4j

Basic Example

```java
// Create a new Hombot Instance
this.bot = new LG6720LVMB("<Your IP>");
// Get the model
this.model = bot.getModel();
this.model.addListener(new HombotModelListener() {
  @Override
  public void onModelChange(int requestId) {
    // do sth when the model changed
  }

  @Override
  public void onRequest(JsonRequest request) {
    // do sth before the request is sent to the bot
    super.onRequest(request);
  }

  @Override
  public void onResponse(JsonConnection conn, JsonResponse response) {
    super.onResponse(conn, response);
  }
});

// Connect to your bot
this.bot.connect();

// Create a new Joystick Control for the bot
JoystickControlKeyAdapter adapter = new JoystickControlKeyAdapter(this.bot);
// Map your KeyCodes to Joystick directions
adapter.mapDirectionToKeyCodes(JoystickDirection.FORWARD, KeyEvent.VK_UP);
// You can specify more than one key, which has to be pressed for a direction
adapter.mapDirectionToKeyCodes(JoystickDirection.BACKWARD_RIGHT, KeyEvent.VK_DOWN, KeyEvent.VK_RIGHT);
// Register the Keylistener at the JFrame to capture KeyEvents
this.addKeyListener(adapter);

```