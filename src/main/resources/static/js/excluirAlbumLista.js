// Adicione um ouvinte de eventos aos botões de exclusão
document.querySelectorAll('.excluir').forEach(function(button) {
    button.addEventListener('click', function(event) {
        event.preventDefault();
        const albumId = this.dataset.albumId;
        const row = this.closest('tr');

        if (confirm('Confirma a exclusão deste álbum? Esta ação irá remover também todas as músicas relacionadas.')) {
            fetch(`/biblioteca/albuns/excluir/${albumId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                },
            })
            .then(response => {
                if (response.ok) {
                    console.log('Álbum excluído com sucesso.');
                    // Remove a linha da tabela
                    if (row) {
                        row.remove();
                    }
                    alert('Álbum excluído com sucesso!');
                    // Recarrega a página para atualizar os contadores de estatísticas
                    location.reload();
                } else {
                    console.error('Erro ao excluir álbum. Status:', response.status);
                    alert('Erro ao excluir álbum. Tente novamente.');
                }
            })
            .catch(error => {
                console.error('Erro de rede:', error);
                alert('Erro de rede ao excluir álbum.');
            });
        }
    });
});