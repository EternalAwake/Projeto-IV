// Exclusão do álbum na visualização
document.querySelectorAll('.excluir-album').forEach(function(button) {
    button.addEventListener('click', function(event) {
        event.preventDefault();
        const albumId = this.dataset.albumId;
        const albumNome = this.dataset.albumNome;

        if (confirm(`Tem certeza que deseja excluir o álbum "${albumNome}"? Esta ação irá remover também todas as músicas relacionadas.`)) {
            fetch(`/biblioteca/albuns/visualizar/excluir/${albumId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                },
            })
            .then(response => {
                if (response.ok) {
                    console.log('Álbum excluído com sucesso.');
                    alert('Álbum excluído com sucesso!');
                    // Redireciona para a listagem de álbuns
                    window.location.href = '/biblioteca/albuns';
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