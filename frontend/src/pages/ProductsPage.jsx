import { useState, useEffect } from 'react';
import { Plus, Search, Edit, Trash2 } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';

export default function ProductsPage() {
    const navigate = useNavigate();
    const [search, setSearch] = useState('');
    const [produtos, setProdutos] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        api.get('/produtos')
            .then(res => {
                setProdutos(res.data);
                setLoading(false);
            })
            .catch(err => {
                console.error("Erro ao carregar produtos:", err);
                alert("Erro ao conectar com backend. Spring Boot tá rodando na porta 8080?");
                setLoading(false);
            });
    }, []);

    const deletar = (id) => {
        if (window.confirm("Tem certeza que quer excluir este produto?")) {
            api.delete(`/produtos/${id}`)
                .then(() => {
                    setProdutos(produtos.filter(p => p.id !== id));
                    alert("Produto excluído com sucesso!");
                })
                .catch(() => alert("Erro ao excluir"));
        }
    };

    const filtered = produtos.filter(p =>
        p.codigo?.toLowerCase().includes(search.toLowerCase()) ||
        p.descricao?.toLowerCase().includes(search.toLowerCase()) ||
        p.marca?.toLowerCase().includes(search.toLowerCase())
    );

    if (loading) return <div className="p-20 text-center text-3xl">Carregando produtos...</div>;

    return (
        <div className="p-6 lg:p-8 text-white">
            <div className="flex flex-col lg:flex-row justify-between items-start lg:items-center mb-8 gap-4">
                <h1 className="text-4xl font-bold">PRODUTOS</h1>
                <button
                    onClick={() => navigate('/produtos/novo')}
                    className="bg-cyan-500 hover:bg-cyan-400 text-black font-bold py-4 px-8 rounded-xl flex items-center gap-3 transition transform hover:scale-105 shadow-2xl"
                >
                    <Plus className="w-6 h-6" />
                    NOVO PRODUTO
                </button>
            </div>

            <div className="relative max-w-md mb-8">
                <Search className="absolute left-4 top-4 w-5 h-5 text-gray-400" />
                <input
                    type="text"
                    placeholder="Buscar por código, descrição ou marca..."
                    value={search}
                    onChange={e => setSearch(e.target.value)}
                    className="w-full pl-12 pr-6 py-4 bg-gray-900/70 border border-gray-800 rounded-xl focus:border-cyan-500 outline-none transition"
                />
            </div>

            <div className="bg-gray-900/70 rounded-2xl border border-gray-800 overflow-hidden">
                <table className="w-full">
                    <thead className="bg-gray-800/50">
                        <tr>
                            <th className="text-left p-6 text-cyan-400 font-bold">CÓDIGO</th>
                            <th className="text-left p-6 text-cyan-400 font-bold">DESCRIÇÃO</th>
                            <th className="text-left p-6 text-cyan-400 font-bold">MARCA</th>
                            <th className="text-center p-6 text-cyan-400 font-bold">ESTOQUE</th>
                            <th className="text-right p-6 text-cyan-400 font-bold">PREÇO</th>
                            <th className="text-center p-6 text-cyan-400 font-bold">AÇÕES</th>
                        </tr>
                    </thead>
                    <tbody>
                        {filtered.map(p => (
                            <tr key={p.id} className="border-b border-gray-800 hover:bg-gray-800/50 transition">
                                <td className="p-6 font-mono text-sm">{p.codigoProduto}</td>
                                <td className="p-6">{p.descricao}</td>
                                <td className="p-6">{p.marca || '-'}</td>
                                <td className="p-6 text-center">
                                    <span className={`px-4 py-1 rounded-full text-sm font-bold ${p.estoqueAtual <= 10 ? 'bg-red-900/60 text-red-300' : 'bg-green-900/60 text-green-300'}`}>
                                        {p.estoqueAtual}
                                    </span>
                                </td>
                                <td className="p-6 text-right font-bold">R$ {Number(p.precoVenda).toFixed(2).replace('.', ',')}</td>
                                <td className="p-6 text-center">
                                    <button onClick={() => navigate(`/produtos/editar/${p.id}`)} className="p-3 hover:bg-gray-700 rounded-lg mx-1 transition">
                                        <Edit className="w-5 h-5 text-cyan-400" />
                                    </button>
                                    <button onClick={() => deletar(p.id)} className="p-3 hover:bg-red-900/50 rounded-lg mx-1 transition">
                                        <Trash2 className="w-5 h-5 text-red-400" />
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>

                {filtered.length === 0 && (
                    <div className="p-20 text-center">
                        <p className="text-3xl text-gray-400">Nenhum produto encontrado</p>
                        <p className="text-cyan-400 mt-4">Cadastre o primeiro produto agora!</p>
                    </div>
                )}
            </div>
        </div>
    );
}