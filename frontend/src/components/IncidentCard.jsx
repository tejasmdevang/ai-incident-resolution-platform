function IncidentCard({ incident, onSelect }) {
    return (
      <div>
        <h3>{incident.title}</h3>
  
        <p>Service: {incident.serviceName}</p>
        <p>Severity: {incident.severity}</p>
        <p>Status: {incident.status}</p>
  
        <button onClick={() => onSelect(incident)}>
          View Details
        </button>
      </div>
    );
  }
  
  export default IncidentCard;