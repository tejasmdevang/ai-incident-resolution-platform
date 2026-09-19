import { useEffect, useState } from "react";
import IncidentCard from "./components/IncidentCard";
import "./App.css";

function App() {
  const [token, setToken] = useState(localStorage.getItem("token"));
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [incidents, setIncidents] = useState([]);
  const [error, setError] = useState("");

  const [selectedIncident, setSelectedIncident] = useState(null);

  const [investigation, setInvestigation] = useState(null);
  const [analyzing, setAnalyzing] = useState(false);

  const [agentInstruction, setAgentInstruction] = useState("");
  const [agentResponse, setAgentResponse] = useState("");
  const [agentRunning, setAgentRunning] = useState(false);

  const [isRegistering, setIsRegistering] = useState(false);
const [name, setName] = useState("");
const [authMessage, setAuthMessage] = useState("");

  // Load incidents after login
  useEffect(() => {
    if (!token) {
      return;
    }

    fetch("http://localhost:8080/api/incidents", {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
      .then((response) => response.json())
      .then((data) => {
        setIncidents(data.content);
      })
      .catch((error) => {
        console.error("Failed to load incidents:", error);
      });
  }, [token]);

  // Login
  function handleLogin(event) {
    event.preventDefault();
    setError("");

    fetch("http://localhost:8080/api/auth/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        email,
        password,
      }),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Login failed");
        }

        return response.json();
      })
      .then((data) => {
        localStorage.setItem("token", data.token);
        setToken(data.token);
      })
      .catch(() => {
        setError("Invalid email or password");
      });
  }

  async function handleRegister(event) {
    event.preventDefault();
  
    setError("");
    setAuthMessage("");
  
    try {
      const response = await fetch(
        "http://localhost:8080/api/auth/register",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            name,
            email,
            password,
          }),
        }
      );
  
      if (!response.ok) {
        throw new Error("Registration failed");
      }
  
      setAuthMessage("Account created successfully. Please sign in.");
      setIsRegistering(false);
      setName("");
      setPassword("");
    } catch (error) {
      console.error("Registration failed:", error);
      setError("Unable to create account.");
    }
  }

  // AI investigation
  async function analyzeIncident() {
    if (!selectedIncident) {
      return;
    }

    setAnalyzing(true);
    setInvestigation(null);

    try {
      const response = await fetch(
        `http://localhost:8080/api/incidents/${selectedIncident.id}/analyze`,
        {
          method: "POST",
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (!response.ok) {
        throw new Error("Investigation failed");
      }

      const data = await response.json();
      setInvestigation(data);
    } catch (error) {
      console.error("AI investigation failed:", error);
    } finally {
      setAnalyzing(false);
    }
  }

  // Agentic incident action
  async function runAgent() {
    if (!selectedIncident || !agentInstruction.trim()) {
      return;
    }

    setAgentRunning(true);
    setAgentResponse("");

    try {
      const response = await fetch(
        `http://localhost:8080/api/incidents/${selectedIncident.id}/agent`,
        {
          method: "POST",
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "text/plain",
          },
          body: agentInstruction,
        }
      );

      if (!response.ok) {
        throw new Error("Agent action failed");
      }

      const message = await response.text();
      setAgentResponse(message);

      // Fetch the incident again because the agent may have changed its status
      const updatedResponse = await fetch(
        `http://localhost:8080/api/incidents/${selectedIncident.id}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (updatedResponse.ok) {
        const updatedIncident = await updatedResponse.json();

        // Update Incident Details
        setSelectedIncident(updatedIncident);

        // Update the incident card in the list
        setIncidents((currentIncidents) =>
          currentIncidents.map((incident) =>
            incident.id === updatedIncident.id
              ? updatedIncident
              : incident
          )
        );
      }
    } catch (error) {
      console.error("Agent action failed:", error);
      setAgentResponse("Agent action failed.");
    } finally {
      setAgentRunning(false);
    }
  }

  if (!token) {
    return (
      <div className="login-page">
        <div className="login-card">
          <div className="login-brand">
            <span className="ai-badge">AI Powered</span>
  
            <h1>AI Incident Resolution Platform</h1>
  
            <p>
              {isRegistering
                ? "Create an account to access the incident response platform."
                : "Sign in to monitor, investigate, and resolve production incidents."}
            </p>
          </div>
  
          <form
            className="login-form"
            onSubmit={isRegistering ? handleRegister : handleLogin}
          >
            {isRegistering && (
              <>
                <label>Name</label>
                <input
                  type="text"
                  placeholder="Your name"
                  value={name}
                  onChange={(event) => setName(event.target.value)}
                  required
                />
              </>
            )}
  
            <label>Email</label>
            <input
              type="email"
              placeholder="you@example.com"
              value={email}
              onChange={(event) => setEmail(event.target.value)}
              required
            />
  
            <label>Password</label>
            <input
              type="password"
              placeholder="Enter your password"
              value={password}
              onChange={(event) => setPassword(event.target.value)}
              required
            />
  
            <button type="submit">
              {isRegistering ? "Create Account" : "Sign In"}
            </button>
  
            {error && <p className="login-error">{error}</p>}
  
            {authMessage && (
              <p className="auth-success">{authMessage}</p>
            )}
          </form>
  
          <div className="auth-switch">
            <span>
              {isRegistering
                ? "Already have an account?"
                : "Don't have an account?"}
            </span>
  
            <button
              type="button"
              className="auth-switch-button"
              onClick={() => {
                setIsRegistering(!isRegistering);
                setError("");
                setAuthMessage("");
              }}
            >
              {isRegistering ? "Sign In" : "Create Account"}
            </button>
          </div>
        </div>
      </div>
    );
  }

  function handleLogout() {
    localStorage.removeItem("token");
  
    setToken(null);
    setSelectedIncident(null);
    setInvestigation(null);
    setAgentResponse("");
  }

  // Main dashboard
  return (
    <div className="app">
      <header className="header">
        <div>
          <h1>AI Incident Resolution Platform</h1>
          <p>Monitor, investigate, and resolve production incidents.</p>
        </div>

        <div className="header-actions">
  <span className="ai-badge">AI Powered</span>

  <button className="logout-button" onClick={handleLogout}>
    Logout
  </button>
</div>
      </header>

      <main className="dashboard">
        {/* Left side - Incident list */}
        <section className="incident-panel">
          <h2>Incidents</h2>

          {incidents.map((incident) => (
            <IncidentCard
              key={incident.id}
              incident={incident}
              onSelect={setSelectedIncident}
            />
          ))}
        </section>

        {/* Right side - Incident details */}
        <section className="details-panel">
          {!selectedIncident && (
            <div className="empty-state">
              <h2>Select an incident</h2>
              <p>
                Choose an incident to view details and run an AI investigation.
              </p>
            </div>
          )}

          {selectedIncident && (
            <div>
              <h2>Incident Details</h2>

              <h3>{selectedIncident.title}</h3>

              <p>{selectedIncident.description}</p>

              <p>
                <strong>Service:</strong> {selectedIncident.serviceName}
              </p>

              <div className="incident-meta">
  <span className={`badge severity-${selectedIncident.severity.toLowerCase()}`}>
    {selectedIncident.severity}
  </span>

  <span className={`badge status-${selectedIncident.status.toLowerCase()}`}>
    {selectedIncident.status}
  </span>
</div>
              <button
                onClick={analyzeIncident}
                disabled={analyzing}
              >
                {analyzing ? "Analyzing..." : "Analyze with AI"}
              </button>

              {/* AI Investigation Result */}
              {investigation && (
  <div className="investigation-card">
                  <h2>AI Investigation</h2>

                  <p>
                    <strong>Summary:</strong> {investigation.summary}
                  </p>

                  <p>
                    <strong>Probable Root Cause:</strong>{" "}
                    {investigation.probableRootCause}
                  </p>

                  <p>
                    <strong>Confidence:</strong>{" "}
                    {Math.round(investigation.confidence * 100)}%
                  </p>

                  <h3>Evidence Used</h3>

                  <ul>
                    {investigation.evidenceUsed?.map((evidence, index) => (
                      <li key={index}>{evidence}</li>
                    ))}
                  </ul>

                  <h3>Recommended Actions</h3>

                  <ul>
                    {investigation.recommendedActions?.map((action, index) => (
                      <li key={index}>{action}</li>
                    ))}
                  </ul>
                </div>
              )}

              {/* Agentic Action */}
              <h2>Incident Agent</h2>

              <input
                type="text"
                placeholder="Example: Change status to RESOLVED"
                value={agentInstruction}
                onChange={(event) =>
                  setAgentInstruction(event.target.value)
                }
              />

              <button
                onClick={runAgent}
                disabled={agentRunning}
              >
                {agentRunning ? "Running..." : "Run Agent"}
              </button>

              {agentResponse && (
                <p>
                  <strong>Agent:</strong> {agentResponse}
                </p>
              )}
            </div>
          )}
        </section>
      </main>
    </div>
  );
}

export default App;