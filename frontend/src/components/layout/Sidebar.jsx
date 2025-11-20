import { Home, Package, ShoppingCart, Users, BarChart3, HelpCircle, LogOut, ChevronLeft } from 'lucide-react';
import { NavLink } from 'react-router-dom';

export default function Sidebar() {
  const menu = [
    { icon: Home, label: "Dashboard", to: "/" },
    { icon: ShoppingCart, label: "Vendas", to: "/sales" },
    { icon: Package, label: "Produtos", to: "/produtos" },
    { icon: Users, label: "Clientes", to: "/customers" },
    { icon: BarChart3, label: "Relatórios", to: "/reports" },
    { icon: HelpCircle, label: "Ajuda", to: "/help" },
  ];

  return (
    <div className="w-64 bg-gray-900 text-white h-screen fixed left-0 top-0 flex flex-col">
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
      <nav className="flex-1 p-4">
        {menu.map((item) => (
          <NavLink
            key={item.to}
            to={item.to}
            className={({ isActive }) =>
              `flex items-center gap-3 px-4 py-3 rounded-lg mb-2 transition-all ${
                isActive
                  ? "bg-cyan-500 text-white shadow-lg shadow-cyan-500/20"
                  : "hover:bg-gray-800"
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
        <button className="flex items-center gap-3 px-4 py-3 rounded-lg hover:bg-gray-800 w-full transition">
          <LogOut className="w-5 h-5" />
          <span>Sair</span>
        </button>
      </div>
    </div>
  );
}