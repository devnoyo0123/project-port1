import PaymentForm from '@/components/PaymentForm';
import PortOneScript from '@/components/PortOneScript';

export default function Home() {
  return (
    <main className="flex min-h-screen flex-col items-center justify-start p-24 gap-8">
      <PortOneScript />
      <h1 className="text-4xl font-bold mb-8">포트원 결제 연동</h1>
      <PaymentForm />
    </main>
  );
}
