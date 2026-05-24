// Adicione um ouvinte de eventos aos botões de exclusão de música
document.querySelectorAll('.excluir-musica').forEach(function(button) {
    button.addEventListener('click', function(event) {
        event.preventDefault();
        const musicaNome = this.dataset.musicaNome || 'esta música';

        if (confirm(`Confirma a exclusão da música "${musicaNome}"?`)) {
            const row = this.closest('tr'); // Obtém a linha atual da tabela
            const musicaId = this.dataset.musicaId;

            // URL para exclusão
            fetch(`/biblioteca/musicas/excluir/${musicaId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                },
            })
            .then(response => {
                if (response.ok) {
                    console.log('Música excluída com sucesso.');
                    // Remove a linha da tabela após a exclusão
                    if (row) {
                        row.remove();
                    }
                    alert('Música excluída com sucesso!');

                    // Opcional: atualizar contadores na página
                    const totalMusicasSpan = document.querySelector('.fw-bold[th\\:text*="totalMusicas"]');
                    if (totalMusicasSpan) {
                        const currentTotal = parseInt(totalMusicasSpan.textContent);
                        totalMusicasSpan.textContent = currentTotal - 1;
                    }
                } else {
                    console.error('Erro ao excluir música. Status:', response.status);
                    alert('Erro ao excluir música. Tente novamente.');
                }
            })
            .catch(error => {
                console.error('Erro de rede:', error);
                alert('Erro de rede ao excluir música.');
            });
        }
    });
});