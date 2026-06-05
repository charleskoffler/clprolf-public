# ☀️ WeatherApp MVC in Clprolf Framework

Everyone knows the **MVC pattern**.

Let’s look at a simple example:
a *WeatherApp* written in Java + Clprolf framework —
which behaves like a **Spring MVC** application,
but runs locally in Swing.

---

## 🧠 1. The Idea Behind It

In Spring MVC, a `Controller` receives a request,
calls a `Repository` or `Service`,
and returns a `View`.

In **Clprolf**, we do exactly the same —
but we **explicitly declare the roles** of each component.

| Component           | Clprolf Role                   | Description                                 |
| ------------------- | ------------------------------ | ------------------------------------------- |
| `WeatherApp`        | `@ClWorker`                      | The system launcher (like Spring Boot main) |
| `WeatherController` | `@ClAgent`                       | The “brain” that coordinates the logic      |
| `WeatherRepository` | `@ClWorker`                      | Technical layer fetching data               |
| `WeatherRenderer`   | `@ClWorker`                      | The View layer (UI and user input)          |

---

## 🏗️ 2. The Complete Code

```java
package org.clprolf.examples.design_patterns.mvc;

import org.clprolf.framework.java.Worker;

@ClWorker
public class WeatherApp {
    public static void main(String[] args) {
        WeatherController controller = new WeatherController();
    }
}
```

---

### 🧩 Controller (Agent Layer)

```java
package org.clprolf.examples.design_patterns.mvc;

import org.clprolf.framework.java.Agent;

@ClAgent
public class WeatherController {
    private WeatherRepository model;
    private WeatherRenderer view;

    public WeatherController() {
        model = new WeatherRepository();
        view = new WeatherRenderer(this);
    }

    public void giveTheWeather(String location){
        model.setLocation(location);
        model.fetchWeather();
        String forecast = model.getForecast();
        view.displayForecast(forecast);
    }
}
```

This class acts like a **Spring `@Controller`**:
it receives a request (`giveTheWeather`),
calls the **repository**,
and updates the **view**.

---

### 🎨 View (Worker Layer)

```java
package org.clprolf.examples.design_patterns.mvc;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import org.clprolf.framework.java.Agent;
import org.clprolf.framework.java.Worker;

@ClWorker
public class WeatherRenderer {

    private JFrame frame;
    private JTextField locationField;
    private JTextArea forecastArea;
    private WeatherController expert;

    @ClAgent
    @ClFamily
    private static interface WindowObserver extends ActionListener { }

    @ClAgent
    private class WindowObserverImpl implements WindowObserver {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            String location = getLocationInput();
            this.expert.giveTheWeather(location);
        }
    }

    public WeatherRenderer(WeatherController expert) {
        this.expert = expert;
        prepareViewObjects();
    }

    protected void prepareViewObjects() {
        frame = new JFrame("Weather Forecast");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        JPanel panel = new JPanel(new BorderLayout());
        JLabel locationLabel = new JLabel("Enter Location:");
        locationField = new JTextField(20);
        locationField.addActionListener(new WindowObserverImpl());

        forecastArea = new JTextArea(5, 30);
        forecastArea.setEditable(false);

        panel.add(locationLabel, BorderLayout.NORTH);
        panel.add(locationField, BorderLayout.CENTER);
        panel.add(new JScrollPane(forecastArea), BorderLayout.SOUTH);

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    public String getLocationInput() {
        return locationField.getText();
    }

    public void displayForecast(String forecast) {
        forecastArea.setText(forecast);
    }
}
```

Here, the **view** acts like a web frontend.
It observes user input (capacity-inherited `ActionListener`)
and sends the event to the **controller agent**.

You could almost replace Swing with HTML/JavaScript
and it would behave the same way!

---

### ☁️ Repository (Technical Worker)

```java
package org.clprolf.examples.design_patterns.mvc;

import org.clprolf.framework.java.Worker;

@ClWorker
class WeatherRepository {
    private String location;
    private String forecast;

    public void setLocation(String location) {
        this.location = location;
    }

    public void fetchWeather() {
        this.forecast = "Sunny with a chance of clouds";
    }

    public String getForecast() {
        return forecast;
    }
}
```

A pure worker: no intelligence, no UI — just technical work.
That’s exactly what a `Repository` or `Service` does in Spring MVC.

---

## 🔄 3. The MVC Flow

```
User (View) → WeatherRenderer (worker)
             → WeatherController (agent)
             → WeatherRepository (worker)
             → back to WeatherRenderer
```

The **same logical flow** as Spring MVC —
but here, everything runs locally and instantly.

---

## 🧠 4. Why It Matters

This example shows that **Clprolf framework integrates seamlessly** with existing patterns.
It doesn’t replace MVC, Spring, or OOP —
it simply **clarifies and strengthens** them.

In a Spring app, you’d just change the way communication happens
(HTTP + `@GetMapping` instead of direct calls) —
but your *roles*, *responsibilities*, and *architecture* remain identical.

---

## 💬 5. Final Thoughts

Clprolf makes **architecture visible**.
You no longer guess what a class *is supposed to be*:
you *declare it* — explicitly.

> A `Controller` is an **Agent**.
> A `Repository` is a **Worker**.
> A `Launcher` is a **Worker**.
> A `View` is also a **Worker** — the interface between human and machine.

In short:
Clprolf doesn’t reinvent MVC — it **makes it self-explanatory**.

---

### 🧡 Summary

| Traditional Role | Clprolf Equivalent             | Layer            |
| ---------------- | ------------------------------ | ---------------- |
| Controller       | `@ClAgent`                     | Logic / Domain   |
| Repository       | `@ClWorker`                    | Data / Technical |
| View             | `@ClWorker`                    | Presentation     |
| Launcher         | `@ClWorker`                    | System entry     |

---
