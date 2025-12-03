import { useState, useEffect } from 'react';
import { Plus, Search, Edit, Trash2, Users, AlertCircle } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';

export default function ClientsPage() {
  const navigate = useNavigate();
  const [search, setSearch] = useState('');
  const [clientes, setClientes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [deleteModal, setDeleteModal] = useState({ open: false, id: null, nome: '' });

  useEffect(() => {
    carregarClientes();
  }, []);

  const carregarClientes = async () => {
    try {
      setLoading(true);
      const res = await api.get('/clientes');
      console.log('Clientes carregados:', res.data); // Debug
      setClientes(res.data);
    } catch (err) {
      console.error('Erro ao carregar clientes:', err);
      alert('Erro ao conectar com o backend. Verifique se está rodando na porta 8080.');
    } finally {
      setLoading(false);
    }
  };

  const confirmarExclusao = (id, nome) => {
    setDeleteModal({ open: true, id, nome });
  };

  const deletar = async () => {
    const { id } = deleteModal;
    try {
      await api.delete(`/clientes/${id}`);
      setClientes(clientes.filter(c => c.id !== id));
      setDeleteModal({ open: false, id: null, nome: '' });
    } catch (err) {
      console.error('Erro ao excluir:', err);
      alert('Erro ao excluir cliente. Tente novamente.');
    }
  };

  const filtered = clientes.filter(c =>
    c.nome?.toLowerCase().includes(search.toLowerCase()) ||
    c.cpf?.includes(search) ||
    c.email?.toLowerCase().includes(search.toLowerCase()) ||
    c.telefone?.includes(search)
  );

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="text-center">
          <div className="animate-spin rounded-full h-16 w-16 border-t-4 border-cyan-500 mx-auto mb-4"></div>
          <p className="text-2xl text-cyan-400">Carregando clientes...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="p-4 lg:p-8 text-white min-h-screen">
      {/* Header */}
      <div className="flex flex-col lg:flex-row justify-between items-start lg:items-center mb-8 gap-4">
        <h1 className="text-3xl lg:text-4xl font-extrabold">CLIENTES</h1>
        <button
          onClick={() => navigate('/clientes/novo')}
          className="bg-cyan-500 hover:bg-cyan-400 text-black font-bold py-3 lg:py-4 px-6 lg:px-8 rounded-xl flex items-center gap-3 transition transform hover:scale-105 shadow-2xl"
        >
          <Plus className="w-5 h-5 lg:w-6 lg:h-6" />
          NOVO CLIENTE
        </button>
      </div>

      {/* Busca */}
      <div className="relative max-w-lg mb-8">
        <Search className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
        <input
          type="text"
          placeholder="Buscar por nome, CPF ou e-mail..."
          value={search}
          onChange={e => setSearch(e.target.value)}
          className="w-full pl-12 pr-6 py-4 bg-gray-900/70 border border-gray-800 rounded-xl focus:border-cyan-500 outline-none transition text-white"
        />
      </div>

      {/* Tabela Desktop */}
      <div className="hidden md:block bg-gray-900/70 backdrop-blur rounded-2xl border border-gray-800 overflow-hidden">
        <table className="w-full">
          <thead className="bg-gray-800/50">
            <tr>
              <th className="text-left p-6 text-cyan-400 font-bold uppercase text-xs">CÓDIGO</th>
              <th className="text-left p-6 text-cyan-400 font-bold uppercase text-xs">NOME</th>
              <th className="text-left p-6 text-cyan-400 font-bold uppercase text-xs hidden lg:table-cell">CPF</th>
              <th className="text-left p-6 text-cyan-400 font-bold uppercase text-xs hidden lg:table-cell">TELEFONE</th>
              <th className="text-left p-6 text-cyan-400 font-bold uppercase text-xs hidden xl:table-cell">E-MAIL</th>
              <th className="text-center p-6 text-cyan-400 font-bold uppercase text-xs">AÇÕES</th>
            </tr>
          </thead>
          <tbody>
            {filtered.map(cliente => (
              <tr key={cliente.id} className="border-b border-gray-800 hover:bg-gray-800/50 transition">
                <td className="p-6 font-mono text-sm">
                  {'CLT' + String(cliente.id).padStart(4, '0')}
                </td>
                <td className="p-6 font-medium">{cliente.nome}</td>
                <td className="p-6 text-gray-400 hidden lg:table-cell">{cliente.cpf || '-'}</td>
                <td className="p-6 text-gray-400 hidden lg:table-cell">{cliente.telefone || '-'}</td>
                <td className="p-6 text-gray-400 hidden xl:table-cell">{cliente.email || '-'}</td>
                <td className="p-6 text-center">
                  <button 
                    onClick={() => navigate(`/clientes/editar/${cliente.id}`)} 
                    className="p-2 hover:bg-gray-700 rounded mx-1 transition"
                    title="Editar"
                  >
                    <Edit className="w-5 h-5 text-cyan-400" />
                  </button>
                  <button 
                    onClick={() => confirmarExclusao(cliente.id, cliente.nome)} 
                    className="p-2 hover:bg-red-900/50 rounded mx-1 transition"
                    title="Excluir"
                  >
                    <Trash2 className="w-5 h-5 text-red-400" />
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>

        {filtered.length === 0 && (
          <div className="p-20 text-center">
            <Users className="w-20 h-20 mx-auto mb-6 text-gray-600" />
            <p className="text-2xl text-gray-400">Nenhum cliente encontrado</p>
            <p className="text-cyan-400 mt-4">
              {search ? 'Tente buscar por outro termo' : 'Cadastre o primeiro cliente agora!'}
            </p>
          </div>
        )}
      </div>

      {/* Cards Mobile */}
      <div className="md:hidden space-y-4">
        {filtered.map(cliente => (
          <div key={cliente.id} className="bg-gray-900/70 backdrop-blur rounded-xl border border-gray-800 p-6">
            <div className="flex justify-between items-start mb-4">
              <div>
                <p className="text-xs text-gray-500 font-mono mb-1">
                  {'CLT' + String(cliente.id).padStart(4, '0')}
                </p>
                <h3 className="text-xl font-bold">{cliente.nome}</h3>
              </div>
            </div>
            
            <div className="space-y-2 mb-4 text-sm text-gray-400">
              {cliente.cpf && <p>CPF: {cliente.cpf}</p>}
              {cliente.telefone && <p>Tel: {cliente.telefone}</p>}
              {cliente.email && <p>Email: {cliente.email}</p>}
            </div>

            <div className="flex gap-2">
              <button 
                onClick={() => navigate(`/clientes/editar/${cliente.id}`)}
                className="flex-1 bg-cyan-600 hover:bg-cyan-500 text-white font-bold py-3 rounded-lg flex items-center justify-center gap-2 transition"
              >
                <Edit className="w-4 h-4" />
                Editar
              </button>
              <button 
                onClick={() => confirmarExclusao(cliente.id, cliente.nome)}
                className="flex-1 bg-red-600 hover:bg-red-500 text-white font-bold py-3 rounded-lg flex items-center justify-center gap-2 transition"
              >
                <Trash2 className="w-4 h-4" />
                Excluir
              </button>
            </div>
          </div>
        ))}

        {filtered.length === 0 && (
          <div className="p-12 text-center bg-gray-900/70 rounded-xl border border-gray-800">
            <Users className="w-16 h-16 mx-auto mb-4 text-gray-600" />
            <p className="text-xl text-gray-400">Nenhum cliente encontrado</p>
            <p className="text-cyan-400 mt-2">
              {search ? 'Tente buscar por outro termo' : 'Cadastre o primeiro cliente!'}
            </p>
          </div>
        )}
      </div>

      {/* Modal de Confirmação de Exclusão */}
      {deleteModal.open && (
        <div className="fixed inset-0 bg-black/80 flex items-center justify-center p-4 z-50">
          <div className="bg-gray-900 rounded-2xl border border-gray-800 p-8 max-w-md w-full">
            <div className="flex items-center gap-4 mb-6">
              <div className="bg-red-900/30 p-3 rounded-full">
                <AlertCircle className="w-8 h-8 text-red-400" />
              </div>
              <h2 className="text-2xl font-bold">Confirmar Exclusão</h2>
            </div>
            
            <p className="text-gray-300 mb-8">
              Tem certeza que deseja excluir o cliente <strong className="text-white">{deleteModal.nome}</strong>? 
              Esta ação não pode ser desfeita.
            </p>

            <div className="flex gap-4">
              <button
                onClick={() => setDeleteModal({ open: false, id: null, nome: '' })}
                className="flex-1 bg-gray-800 hover:bg-gray-700 text-white font-bold py-3 rounded-xl transition"
              >
                Cancelar
              </button>
              <button
                onClick={deletar}
                className="flex-1 bg-red-600 hover:bg-red-500 text-white font-bold py-3 rounded-xl transition"
              >
                Excluir
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}