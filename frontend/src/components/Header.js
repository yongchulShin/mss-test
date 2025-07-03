import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import styled from 'styled-components';

const HeaderContainer = styled.header`
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 1rem 0;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
`;

const Nav = styled.nav`
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
`;

const Logo = styled(Link)`
  font-size: 1.5rem;
  font-weight: bold;
  color: white;
  text-decoration: none;
  
  &:hover {
    color: #f0f0f0;
  }
`;

const NavLinks = styled.div`
  display: flex;
  gap: 2rem;
`;

const NavLink = styled(Link)`
  color: white;
  text-decoration: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  transition: background-color 0.3s;
  
  &:hover {
    background-color: rgba(255,255,255,0.1);
  }
  
  ${props => props.active && `
    background-color: rgba(255,255,255,0.2);
  `}
`;

function Header() {
  const location = useLocation();

  return (
    <HeaderContainer>
      <Nav>
        <Logo to="/">Musinsa Backend</Logo>
        <NavLinks>
          <NavLink to="/" active={location.pathname === '/'}>
            대시보드
          </NavLink>
          {/*<NavLink to="/brands" active={location.pathname === '/brands'}>*/}
          {/*  브랜드 관리*/}
          {/*</NavLink>*/}
          <NavLink to="/analysis" active={location.pathname === '/analysis'}>
            상품 분석
          </NavLink>
        </NavLinks>
      </Nav>
    </HeaderContainer>
  );
}

export default Header; 