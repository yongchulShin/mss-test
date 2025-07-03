import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import styled from 'styled-components';
import Header from './components/Header';
import Dashboard from './pages/Dashboard';
import BrandManagement from './pages/BrandManagement';
import ProductAnalysis from './pages/ProductAnalysis';

const AppContainer = styled.div`
  min-height: 100vh;
  background-color: #f8f9fa;
`;

const MainContent = styled.main`
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
`;

function App() {
  return (
    <Router>
      <AppContainer>
        <Header />
        <MainContent>
          <Routes>
            <Route path="/" element={<Dashboard />} />
            <Route path="/brands" element={<BrandManagement />} />
            <Route path="/analysis" element={<ProductAnalysis />} />
          </Routes>
        </MainContent>
      </AppContainer>
    </Router>
  );
}

export default App; 