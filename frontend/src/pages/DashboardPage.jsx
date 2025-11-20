import { DollarSign, ShoppingBag, Package, TrendingUp } from 'lucide-react';

export default function DashboardPage() {
  return (
    <div className="p-8 text-white">
      {/* Título */}
      <h1 className="text-4xl font-bold mb-8">Dashboard</h1>

      {/* 4 Cards de Estatísticas */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-10">
        
        {/* Receita Total */}
        <div className="bg-gray-900/80 backdrop-blur rounded-2xl p-6 border border-gray-800 hover:border-cyan-500 transition-all">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-gray-400 text-sm">Receita Total</p>
              <p className="text-3xl font-bold mt-2">R$ 250.000</p>
              <p className="text-green-400 text-sm mt-2">+12.5% vs mês passado</p>
            </div>
            <DollarSign className="w-12 h-12 text-green-400 opacity-80" />
          </div>
        </div>

        {/* Vendas Hoje */}
        <div className="bg-gray-900/80 backdrop-blur rounded-2xl p-6 border border-gray-800 hover:border-cyan-500 transition-all">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-gray-400 text-sm">Vendas Hoje</p>
              <p className="text-3xl font-bold mt-2">87</p>
              <p className="text-green-400 text-sm mt-2">+8% vs ontem</p>
            </div>
            <ShoppingBag className="w-12 h-12 text-cyan-400 opacity-80" />
          </div>
        </div>

        {/* Produtos em Estoque */}
        <div className="bg-gray-900/80 backdrop-blur rounded-2xl p-6 border border-gray-800 hover:border-purple-500 transition-all">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-gray-400 text-sm">Produtos em Estoque</p>
              <p className="text-3xl font-bold mt-2">1.247</p>
            </div>
            <Package className="w-12 h-12 text-purple-400 opacity-80" />
          </div>
        </div>

        {/* Pedidos Pendentes */}
        <div className="bg-gray-900/80 backdrop-blur rounded-2xl p-6 border border-gray-800 hover:border-orange-500 transition-all">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-gray-400 text-sm">Pedidos Pendentes</p>
              <p className="text-3xl font-bold mt-2 text-orange-400">15</p>
            </div>
            <TrendingUp className="w-12 h-12 text-orange-400 opacity-80" />
          </div>
        </div>
      </div>

      {/* Só pra você ver que tá vivo */}
      <div className="text-center text-5xl animate-pulse">
        🛞 BEM-VINDO À DASHBOARD DOS SONHOS 🛞
      </div>
    </div>
  );
}