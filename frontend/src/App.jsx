import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Sidebar from './components/layout/Sidebar';
import DashboardPage from './pages/DashboardPage.jsx';

import ProductsPage from './pages/ProductsPage.jsx';
import ProductFormPage from './pages/ProductFormPage.jsx';

import ClientsPage from './pages/ClientsPage.jsx';
import ClientFormPage from './pages/ClientFormPage.jsx';

import OrcamentoFormPage from './pages/OrcamentoFormPage.jsx';


export default function App() {
  return (
    <BrowserRouter>
      <div className="flex min-h-screen bg-gray-950">
        <Sidebar />
        <main className="flex-1 lg:ml-64 p-4 lg:p-8">
          <Routes>
            <Route path="/" element={<DashboardPage />} />
            <Route path="/produtos" element={<ProductsPage />} />
            <Route path="/produtos/novo" element={<ProductFormPage />} />
            <Route path="/produtos/editar/:id" element={<ProductFormPage />} />
            <Route path="/clientes" element={<ClientsPage />} />
            <Route path="/clientes/novo" element={<ClientFormPage />} />
            <Route path="/clientes/editar/:id" element={<ClientFormPage />} />
            <Route path="/orcamentos" element={<OrcamentoFormPage />} />
            <Route path="/orcamentos/novo" element={<OrcamentoFormPage />} />
            <Route path="/orcamentos/editar/:id" element={<OrcamentoFormPage />} />

          </Routes>
        </main>
      </div>
    </BrowserRouter>
  );
}