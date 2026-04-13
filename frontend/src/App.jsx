import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Plane, User, LayoutDashboard, CheckCircle, XCircle, Clock, PlusCircle } from 'lucide-react';

const API_BASE = 'http://localhost:8080/api';

const App = () => {
  const [currentUser, setCurrentUser] = useState(null);
  const [users, setUsers] = useState([]);
  const [activeTab, setActiveTab] = useState('dashboard');
  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    fetchUsers();
    fetchRequests();
  }, []);

  const fetchUsers = async () => {
    try {
      const res = await axios.get(`${API_BASE}/users`);
      setUsers(res.data);
      if (res.data.length > 0) setCurrentUser(res.data[0]);
    } catch (err) {
      console.error("Error fetching users", err);
    }
  };

  const fetchRequests = async () => {
    setLoading(true);
    try {
      const res = await axios.get(`${API_BASE}/travel-requests`);
      setRequests(res.data);
    } catch (err) {
      console.error("Error fetching requests", err);
    } finally {
      setLoading(false);
    }
  };

  const submitRequest = async (id) => {
    await axios.put(`${API_BASE}/travel-requests/${id}/submit`);
    fetchRequests();
  };

  const managerApprove = async (id) => {
    await axios.put(`${API_BASE}/travel-requests/${id}/manager-approve`);
    fetchRequests();
  };

  const financeApprove = async (id) => {
    try {
      await axios.put(`${API_BASE}/travel-requests/${id}/finance-approve`);
      fetchRequests();
    } catch (err) {
      alert("Finance Approval Failed: " + (err.response?.data?.message || err.message));
    }
  };

  const bookRequest = async (id) => {
    await axios.put(`${API_BASE}/travel-requests/${id}/book`);
    fetchRequests();
  };

  return (
    <div className="min-h-screen">
      <nav className="nav">
        <div className="flex items-center gap-2" style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
          <Plane className="text-primary" size={32} />
          <h1 style={{ fontSize: '1.5rem', fontWeight: 'bold' }}>TravelFlow</h1>
        </div>
        <div style={{ display: 'flex', alignItems: 'center', gap: '20px' }}>
          <select 
            className="btn btn-outline"
            style={{ width: 'auto' }}
            value={currentUser?.id || ''} 
            onChange={(e) => setCurrentUser(users.find(u => u.id == e.target.value))}
          >
            {users.map(u => (
              <option key={u.id} value={u.id}>{u.name} ({u.role})</option>
            ))}
          </select>
          <div style={{ color: 'var(--text-muted)' }}>
            Role: <strong>{currentUser?.role}</strong>
          </div>
        </div>
      </nav>

      <main className="container">
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 3fr', gap: '2rem' }}>
          <aside>
            <div className="card" style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
              <button className={`btn ${activeTab === 'dashboard' ? 'btn-primary' : 'btn-outline'}`} onClick={() => setActiveTab('dashboard')}>
                <LayoutDashboard size={20} /> Dashboard
              </button>
              {currentUser?.role === 'EMPLOYEE' && (
                <button className={`btn ${activeTab === 'new' ? 'btn-primary' : 'btn-outline'}`} onClick={() => setActiveTab('new')}>
                  <PlusCircle size={20} /> New Request
                </button>
              )}
            </div>
          </aside>

          <section>
            {activeTab === 'dashboard' ? (
              <div style={{ display: 'flex', flexDirection: 'column', gap: '1.5rem' }}>
                <h2 style={{ fontSize: '1.25rem' }}>Travel Requests</h2>
                {loading ? <p>Loading...</p> : requests.map(req => (
                  <RequestCard 
                    key={req.id} 
                    request={req} 
                    user={currentUser} 
                    onAction={{ submitRequest, managerApprove, financeApprove, bookRequest }}
                  />
                ))}
              </div>
            ) : (
              <NewRequestForm user={currentUser} onSuccess={() => { setActiveTab('dashboard'); fetchRequests(); }} />
            )}
          </section>
        </div>
      </main>
    </div>
  );
};

const RequestCard = ({ request, user, onAction }) => {
  const getStatusClass = (status) => {
    if (status.includes('APPROVED') || status === 'COMPLETED') return 'status-approved';
    if (status.includes('REJECTED') || status === 'CANCELLED') return 'status-rejected';
    if (status === 'SUBMITTED') return 'status-submitted';
    if (status === 'BOOKED') return 'status-booked';
    return 'status-draft';
  };

  return (
    <div className="card" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
      <div>
        <div style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
          <h3 style={{ fontSize: '1.1rem' }}>{request.requestNumber}</h3>
          <span className={`status-badge ${getStatusClass(request.status)}`}>{request.status}</span>
        </div>
        <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem', marginTop: '0.5rem' }}>
          {request.fromCity} → {request.toCity} | {request.travelType} | ₹{request.estimatedCost}
        </p>
        <p style={{ fontSize: '0.8rem', marginTop: '0.2rem' }}>Date: {request.fromDate} to {request.toDate}</p>
      </div>

      <div style={{ display: 'flex', gap: '0.5rem' }}>
        {request.status === 'DRAFT' && user.id === request.employee.id && (
          <button className="btn btn-primary" onClick={() => onAction.submitRequest(request.id)}>Submit</button>
        )}
        {request.status === 'SUBMITTED' && user.role === 'MANAGER' && (
          <button className="btn btn-primary" onClick={() => onAction.managerApprove(request.id)}>Approve</button>
        )}
        {request.status === 'MANAGER_APPROVED' && user.role === 'FINANCE' && (
          <button className="btn btn-primary" onClick={() => onAction.financeApprove(request.id)}>Verify Budget & Approve</button>
        )}
        {request.status === 'FINANCE_APPROVED' && user.role === 'TRAVEL_DESK' && (
          <button className="btn btn-primary" onClick={() => onAction.bookRequest(request.id)}>Book Ticket</button>
        )}
      </div>
    </div>
  );
};

const NewRequestForm = ({ user, onSuccess }) => {
  const [formData, setFormData] = useState({
    fromCity: '',
    toCity: '',
    travelType: 'DOMESTIC',
    estimatedCost: '',
    purpose: '',
    fromDate: '',
    toDate: ''
  });

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await axios.post(`${API_BASE}/travel-requests`, {
        ...formData,
        employee: { id: user.id }
      });
      onSuccess();
    } catch (err) {
      alert("Failed to create request");
    }
  };

  return (
    <div className="card">
      <h2 style={{ marginBottom: '1.5rem' }}>Create Travel Request</h2>
      <form onSubmit={handleSubmit} style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1.5rem' }}>
        <div>
          <label>From City</label>
          <input required type="text" value={formData.fromCity} onChange={e => setFormData({...formData, fromCity: e.target.value})} />
        </div>
        <div>
          <label>To City</label>
          <input required type="text" value={formData.toCity} onChange={e => setFormData({...formData, toCity: e.target.value})} />
        </div>
        <div>
          <label>Travel Type</label>
          <select value={formData.travelType} onChange={e => setFormData({...formData, travelType: e.target.value})}>
            <option value="DOMESTIC">Domestic</option>
            <option value="INTERNATIONAL">International</option>
          </select>
        </div>
        <div>
          <label>Estimated Cost (₹)</label>
          <input required type="number" value={formData.estimatedCost} onChange={e => setFormData({...formData, estimatedCost: e.target.value})} />
        </div>
        <div>
          <label>From Date</label>
          <input required type="date" value={formData.fromDate} onChange={e => setFormData({...formData, fromDate: e.target.value})} />
        </div>
        <div>
          <label>To Date</label>
          <input required type="date" value={formData.toDate} onChange={e => setFormData({...formData, toDate: e.target.value})} />
        </div>
        <div style={{ gridColumn: 'span 2' }}>
          <label>Purpose</label>
          <textarea value={formData.purpose} onChange={e => setFormData({...formData, purpose: e.target.value})} />
        </div>
        <div style={{ gridColumn: 'span 2' }}>
          <button type="submit" className="btn btn-primary" style={{ width: '100%' }}>Create Draft</button>
        </div>
      </form>
    </div>
  );
};

export default App;
