import React, { useState } from 'react';
import styled from 'styled-components';
import { brandAPI } from '../services/api';

const Container = styled.div`
  padding: 20px;
`;

const Title = styled.h1`
  color: #333;
  margin-bottom: 30px;
  text-align: center;
`;

const FormContainer = styled.div`
  background: white;
  border-radius: 8px;
  padding: 30px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  max-width: 600px;
  margin: 0 auto;
`;

const Form = styled.form`
  display: flex;
  flex-direction: column;
  gap: 20px;
`;

const FormGroup = styled.div`
  display: flex;
  flex-direction: column;
  gap: 8px;
`;

const Label = styled.label`
  font-weight: 500;
  color: #333;
`;

const Input = styled.input`
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
  
  &:focus {
    outline: none;
    border-color: #667eea;
    box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.2);
  }
`;

const ProductGrid = styled.div`
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 15px;
`;

const ProductCard = styled.div`
  border: 1px solid #ddd;
  border-radius: 4px;
  padding: 15px;
  background: #f9f9f9;
`;

const ProductTitle = styled.h4`
  margin: 0 0 10px 0;
  color: #333;
`;

const Button = styled.button`
  padding: 12px 24px;
  background: #667eea;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
  
  &:hover {
    background: #5a6fd8;
  }
  
  &:disabled {
    background: #ccc;
    cursor: not-allowed;
  }
`;

const Message = styled.div`
  padding: 15px;
  border-radius: 4px;
  margin: 20px 0;
  
  ${props => props.success && `
    background: #d4edda;
    color: #155724;
    border: 1px solid #c3e6cb;
  `}
  
  ${props => props.error && `
    background: #f8d7da;
    color: #721c24;
    border: 1px solid #f5c6cb;
  `}
`;

const categories = [
  '상의', '아우터', '바지', '스니커즈', '가방', '모자', '양말', '액세서리'
];

const initialProducts = categories.map(category => ({
  categoryName: category,
  price: 0
}));

function BrandManagement() {
  const [brandName, setBrandName] = useState('');
  const [products, setProducts] = useState(initialProducts);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState(null);

  const handleProductChange = (index, field, value) => {
    const newProducts = [...products];
    newProducts[index] = {
      ...newProducts[index],
      [field]: field === 'price' ? parseInt(value) || 0 : value
    };
    setProducts(newProducts);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!brandName.trim()) {
      setMessage({ type: 'error', text: '브랜드명을 입력해주세요.' });
      return;
    }

    const hasZeroPrice = products.some(product => product.price <= 0);
    if (hasZeroPrice) {
      setMessage({ type: 'error', text: '모든 상품의 가격을 입력해주세요.' });
      return;
    }

    setLoading(true);
    setMessage(null);

    try {
      await brandAPI.createBrand({
        name: brandName,
        products: products
      });
      
      setMessage({ type: 'success', text: '브랜드가 성공적으로 추가되었습니다!' });
      setBrandName('');
      setProducts(initialProducts);
    } catch (error) {
      const errorMessage = error.response?.data?.message || '브랜드 추가 중 오류가 발생했습니다.';
      setMessage({ type: 'error', text: errorMessage });
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container>
      <Title>브랜드 관리</Title>
      
      {message && (
        <Message success={message.type === 'success'} error={message.type === 'error'}>
          {message.text}
        </Message>
      )}

      <FormContainer>
        <Form onSubmit={handleSubmit}>
          <FormGroup>
            <Label>브랜드명</Label>
            <Input
              type="text"
              value={brandName}
              onChange={(e) => setBrandName(e.target.value)}
              placeholder="브랜드명을 입력하세요"
              required
            />
          </FormGroup>

          <FormGroup>
            <Label>상품 정보 (모든 카테고리에 1개씩 필수)</Label>
            <ProductGrid>
              {products.map((product, index) => (
                <ProductCard key={index}>
                  <ProductTitle>{product.categoryName}</ProductTitle>
                  <Input
                    type="number"
                    value={product.price}
                    onChange={(e) => handleProductChange(index, 'price', e.target.value)}
                    placeholder="가격"
                    min="0"
                    required
                  />
                </ProductCard>
              ))}
            </ProductGrid>
          </FormGroup>

          <Button type="submit" disabled={loading}>
            {loading ? '처리 중...' : '브랜드 추가'}
          </Button>
        </Form>
      </FormContainer>
    </Container>
  );
}

export default BrandManagement; 