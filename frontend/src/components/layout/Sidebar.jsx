import { useState } from 'react';
import { Home, Package, ShoppingCart, Users, BarChart3, HelpCircle, LogOut, Menu, X } from 'lucide-react';
import { NavLink } from 'react-router-dom';

export default function Sidebar() {
  const [mobileOpen, setMobileOpen] = useState(false);

  const menu = [
    { icon: Home, label: "Dashboard", to: "/" },
    { icon: ShoppingCart, label: "Vendas", to: "/sales" },
    { icon: Package, label: "Produtos", to: "/produtos" },
    { icon: Users, label: "Clientes", to: "/clientes" },
    { icon: BarChart3, label: "Relatórios", to: "/reports" },
    { icon: HelpCircle, label: "Ajuda", to: "/help" },
  ];

  const closeMobile = () => setMobileOpen(false);

  return (
    <>
      {/* Botão Mobile (hamburguer) */}
      <button
        onClick={() => setMobileOpen(!mobileOpen)}
        className="lg:hidden fixed top-4 left-4 z-50 p-3 bg-gray-900 rounded-xl shadow-lg hover:bg-gray-800 transition"
      >
        {mobileOpen ? (
          <X className="w-6 h-6 text-white" />
        ) : (
          <Menu className="w-6 h-6 text-white" />
        )}
      </button>

      {/* Overlay Mobile */}
      {mobileOpen && (
        <div 
          className="lg:hidden fixed inset-0 bg-black/60 z-30"
          onClick={closeMobile}
        />
      )}

      {/* Sidebar */}
      <div className={`
        w-64 bg-gray-900 text-white h-screen fixed left-0 top-0 flex flex-col z-40
        transition-transform duration-300
        ${mobileOpen ? 'translate-x-0' : '-translate-x-full lg:translate-x-0'}
      `}>
        {/* Logo */}
        <div className="p-6 border-b border-gray-800">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 bg-cyan-500 rounded-lg flex items-center justify-center text-2xl font-bold">
              B
            </div>
            <h1 className="text-xl font-bold">Borracharia Pro</h1>
          </div>
        </div>

        {/* Menu */}
        <nav className="flex-1 p-4 overflow-y-auto">
          {menu.map((item) => (
            <NavLink
              key={item.to}
              to={item.to}
              onClick={closeMobile}
              className={({ isActive }) =>
                `flex items-center gap-3 px-4 py-3 rounded-lg mb-2 transition-all ${
                  isActive
                    ? "bg-cyan-500 text-white shadow-lg shadow-cyan-500/20"
                    : "hover:bg-gray-800 text-gray-300"
                }`
              }
            >
              <item.icon className="w-5 h-5" />
              <span className="font-medium">{item.label}</span>
            </NavLink>
          ))}
        </nav>

        {/* Logout */}
        <div className="p-4 border-t border-gray-800">
          <button 
            className="flex items-center gap-3 px-4 py-3 rounded-lg hover:bg-gray-800 w-full transition text-gray-300 hover:text-white"
            onClick={() => {
              if (window.confirm('Deseja realmente sair?')) {
                // Lógica de logout
                console.log('Logout');
              }
            }}
          >
            <LogOut className="w-5 h-5" />
            <span>Sair</span>
          </button>
        </div>
      </div>
    </>
  );
}