'use client';

import { useState, useEffect } from 'react';
import { ulid } from 'ulid';
import Script from 'next/script';

declare global {
  interface Window {
    IMP?: any;
  }
}

interface PaymentFormProps {
  // 필요한 props가 있다면 여기에 추가
}

export default function PaymentForm({}: PaymentFormProps) {
  const [storeCode, setStoreCode] = useState('');
  const [productName, setProductName] = useState('');
  const [amount, setAmount] = useState('');
  const [isScriptLoaded, setIsScriptLoaded] = useState(false);

  // 스크립트 로드 완료 후 실행될 함수
  const handleScriptLoad = () => {
    console.log("포트원 스크립트 로드 완료");
    if (window.IMP) {

      const merchantUID = process.env.NEXT_PUBLIC_PORTONE_MERCHANT_UID;
      if (merchantUID) {
        window.IMP.init(merchantUID);
        console.log("포트원 SDK 초기화 완료");
        setIsScriptLoaded(true);
      } else {
        console.error("NEXT_PUBLIC_PORTONE_MERCHANT_UID 환경 변수가 설정되지 않았습니다.");
      }
    }
  };

  // 결제 처리 핸들러
  const handlePayment = () => {
    const { IMP } = window;

    // 유효성 검사
    if (!storeCode || !productName || !amount) {
      alert('모든 필드를 입력해주세요.');
      return;
    }

    // IMP 객체 확인
    if (!IMP) {
      alert('결제 모듈이 로드되지 않았습니다. 페이지를 새로고침한 후 다시 시도해주세요.');
      return;
    }

    // 환경 변수 확인
    const channelKey = process.env.NEXT_PUBLIC_PORTONE_CHANNEL_KEY;
    console.log("channelKey", channelKey);
    if (!channelKey) {
      alert('채널키가 설정되지 않았습니다. 관리자에게 문의하세요.');
      console.error("NEXT_PUBLIC_PORTONE_CHANNEL_KEY 환경 변수가 설정되지 않았습니다.");
      return;
    }

    // 결제 데이터 구성
    const data = {
      channelKey: channelKey, // 포트원 콘솔에서 발급받은 채널키
      pay_method: 'card', // 신용카드 고정
      merchant_uid: `mid_${ulid()}`, // 고유한 주문번호 생성 방식으로 변경
      name: productName,
      amount: Number(amount),
      buyer_email: 'buyer@example.com',
      buyer_name: '구매자명',
      buyer_tel: '010-1234-5678',
      buyer_addr: '서울특별시 강남구 삼성동',
      buyer_postcode: '123-456',
      currency: 'KRW', // 통화 명시적 지정
      digital: false, // 실물 상품 여부
      card: {
        detail: {
          card_code: '*', // 모든 카드사 허용
          enabled: true,
        },
      },
    };

    console.log("결제 요청 데이터:", data);
    
    // 결제창 호출
    IMP.request_pay(data, function(response: any) {
      if (response.success) {
        alert('결제 성공');
        console.log('결제 성공', response);
        
        // // 백엔드 검증 API 호출
        // fetch('/api/payments/verify', {
        //   method: 'POST',
        //   headers: { 'Content-Type': 'application/json' },
        //   body: JSON.stringify({
        //     imp_uid: response.imp_uid,
        //     merchant_uid: response.merchant_uid
        //   }),
        // })
        // .then(res => res.json())
        // .then(data => {
        //   console.log(data);
        //   alert('결제 완료');
        // })
        // .catch(error => {
        //   console.error('결제 검증 중 오류 발생:', error);
        //   alert('결제 검증 중 오류가 발생했습니다.');
        // });
      } else {
        alert(`결제 실패: ${response.error_msg}`);
      }
    });
  };

  return (
    <>
      <Script
        src="https://cdn.iamport.kr/v1/iamport.js"
        strategy="afterInteractive"
        onLoad={handleScriptLoad}
        onError={() => console.error("포트원 스크립트 로드 실패")}
      />
      
      <div className="max-w-xl mx-auto p-6 bg-white rounded-lg shadow-md">
        <h2 className="text-2xl font-bold mb-6 text-center text-gray-800">결제 정보 입력</h2>

        <div className="mb-4">
          <label htmlFor="storeCode" className="block text-sm font-medium text-gray-700 mb-1">
            가맹점 주문번호
          </label>
          <input
            type="text"
            id="storeCode"
            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 min-w-[400px] placeholder-gray-500 text-gray-900"
            value={storeCode}
            onChange={e => setStoreCode(e.target.value)}
            placeholder="가맹점 주문번호를 입력하세요"
          />
        </div>

        <div className="mb-4">
          <label htmlFor="productName" className="block text-sm font-medium text-gray-700 mb-1">
            결제대상 제품명
          </label>
          <input
            type="text"
            id="productName"
            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 placeholder-gray-500 text-gray-900"
            value={productName}
            onChange={e => setProductName(e.target.value)}
            placeholder="제품명을 입력하세요"
          />
        </div>

        <div className="mb-6">
          <label htmlFor="amount" className="block text-sm font-medium text-gray-700 mb-1">
            결제금액
          </label>
          <input
            type="number"
            id="amount"
            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 placeholder-gray-500 text-gray-900"
            value={amount}
            onChange={e => setAmount(e.target.value)}
            placeholder="금액을 입력하세요"
          />
        </div>

        <button
          onClick={handlePayment}
          className="w-full bg-blue-600 text-white py-2 px-4 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition-colors"
          disabled={!isScriptLoaded}
        >
          결제하기
        </button>
      </div>
    </>
  );
}
