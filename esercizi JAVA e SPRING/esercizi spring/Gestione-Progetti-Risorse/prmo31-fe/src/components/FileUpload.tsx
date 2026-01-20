import React, { useRef, useState } from 'react';
import { Upload, X, File, CheckCircle } from 'lucide-react';
import Button from './Button';
import './FileUpload.css';

interface FileUploadProps {
  onUpload: (files: File[]) => void;
  accept?: string;
  multiple?: boolean;
  maxSize?: number; // in MB
  maxFiles?: number;
}

interface UploadedFile {
  file: File;
  progress: number;
  status: 'uploading' | 'success' | 'error';
  error?: string;
}

const FileUpload: React.FC<FileUploadProps> = ({
  onUpload,
  accept = '*',
  multiple = false,
  maxSize = 10,
  maxFiles = 5,
}) => {
  const [files, setFiles] = useState<UploadedFile[]>([]);
  const [dragActive, setDragActive] = useState(false);
  const inputRef = useRef<HTMLInputElement>(null);

  const validateFile = (file: File): string | null => {
    const fileSizeMB = file.size / 1024 / 1024;
    if (fileSizeMB > maxSize) {
      return `File troppo grande (max ${maxSize}MB)`;
    }
    return null;
  };

  const handleFiles = (fileList: FileList) => {
    const newFiles = Array.from(fileList);

    if (!multiple && newFiles.length > 1) {
      alert('Puoi caricare solo un file alla volta');
      return;
    }

    if (files.length + newFiles.length > maxFiles) {
      alert(`Puoi caricare massimo ${maxFiles} file`);
      return;
    }

    const uploadedFiles: UploadedFile[] = newFiles.map((file) => {
      const error = validateFile(file);
      return {
        file,
        progress: 0,
        status: error ? ('error' as const) : ('uploading' as const),
        error: error || undefined,
      };
    });

    setFiles((prev) => [...prev, ...uploadedFiles]);

    // Simulate upload progress
    uploadedFiles.forEach((uploadedFile, index) => {
      if (uploadedFile.status === 'uploading') {
        simulateUpload(files.length + index);
      }
    });

    // Call onUpload with valid files
    const validFiles = newFiles.filter((file) => !validateFile(file));
    if (validFiles.length > 0) {
      onUpload(validFiles);
    }
  };

  const simulateUpload = (index: number) => {
    let progress = 0;
    const interval = setInterval(() => {
      progress += 10;
      setFiles((prev) =>
        prev.map((f, i) =>
          i === index
            ? { ...f, progress: Math.min(progress, 100) }
            : f
        )
      );

      if (progress >= 100) {
        clearInterval(interval);
        setFiles((prev) =>
          prev.map((f, i) =>
            i === index ? { ...f, status: 'success' as const } : f
          )
        );
      }
    }, 200);
  };

  const handleDrag = (e: React.DragEvent) => {
    e.preventDefault();
    e.stopPropagation();
    if (e.type === 'dragenter' || e.type === 'dragover') {
      setDragActive(true);
    } else if (e.type === 'dragleave') {
      setDragActive(false);
    }
  };

  const handleDrop = (e: React.DragEvent) => {
    e.preventDefault();
    e.stopPropagation();
    setDragActive(false);

    if (e.dataTransfer.files && e.dataTransfer.files[0]) {
      handleFiles(e.dataTransfer.files);
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files[0]) {
      handleFiles(e.target.files);
    }
  };

  const removeFile = (index: number) => {
    setFiles((prev) => prev.filter((_, i) => i !== index));
  };

  const formatFileSize = (bytes: number): string => {
    if (bytes === 0) return '0 Bytes';
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return Math.round(bytes / Math.pow(k, i) * 100) / 100 + ' ' + sizes[i];
  };

  return (
    <div className="file-upload-container">
      <div
        className={`file-upload-dropzone ${dragActive ? 'active' : ''}`}
        onDragEnter={handleDrag}
        onDragLeave={handleDrag}
        onDragOver={handleDrag}
        onDrop={handleDrop}
        onClick={() => inputRef.current?.click()}
      >
        <Upload size={48} />
        <h3>Trascina qui i file o clicca per selezionare</h3>
        <p>
          {accept !== '*' ? `Formati accettati: ${accept}` : 'Tutti i formati'}
          {' | '}
          Dimensione massima: {maxSize}MB
        </p>
        <input
          ref={inputRef}
          type="file"
          accept={accept}
          multiple={multiple}
          onChange={handleChange}
          style={{ display: 'none' }}
        />
        <Button variant="primary" icon={<Upload size={20} />}>
          Seleziona File
        </Button>
      </div>

      {files.length > 0 && (
        <div className="uploaded-files-list">
          {files.map((uploadedFile, index) => (
            <div key={index} className={`uploaded-file ${uploadedFile.status}`}>
              <div className="file-icon">
                <File size={24} />
              </div>
              <div className="file-info">
                <div className="file-name">{uploadedFile.file.name}</div>
                <div className="file-size">{formatFileSize(uploadedFile.file.size)}</div>
                {uploadedFile.status === 'uploading' && (
                  <div className="upload-progress">
                    <div
                      className="progress-bar"
                      style={{ width: `${uploadedFile.progress}%` }}
                    ></div>
                  </div>
                )}
                {uploadedFile.error && (
                  <div className="file-error">{uploadedFile.error}</div>
                )}
              </div>
              <div className="file-status">
                {uploadedFile.status === 'success' && (
                  <CheckCircle size={20} className="success-icon" />
                )}
                <button
                  className="remove-file"
                  onClick={() => removeFile(index)}
                  type="button"
                >
                  <X size={20} />
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default FileUpload;
